package com.example.geotracker.presentation.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.geotracker.R;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;
import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.map.events.PathEvent;
import com.example.geotracker.presentation.map.events.MapEvent;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.utils.DateTimeUtils;
import com.example.geotracker.utils.SingleLiveEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class MapViewModel extends ViewModel {
    @NonNull
    private RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor;
    @NonNull
    private PersistInteractor<Void, VisibleJourney> persistSingleJourneyInteractor;
    @NonNull
    private RetrieveInteractor<Void, List<VisibleLocation>> retrieveVisibleLocationsForActiveJourneyInteractor;

    @NonNull
    private MutableLiveData<PermissionsRequestEvent> permissionsRequestLiveEvent;
    @NonNull
    private MutableLiveData<Void> permissionGrantLiveEvent;
    @NonNull
    private MutableLiveData<Boolean> trackingStateStreamEvent;
    @NonNull
    private MutableLiveData<MapEvent> mapEventsObservableStream;
    @NonNull
    private MutableLiveData<PathEvent> journeyEventsObservableStream;

    @NonNull
    private CompositeDisposable compositeDisposable;
    @Nullable
    private Disposable activePathUpdatesConsumerDisposable;

    @Inject
    MapViewModel(@NonNull @ActiveJourneys RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor,
                 @NonNull PersistInteractor<Void, VisibleJourney> persistSingleJourneyInteractor,
                 @NonNull RetrieveInteractor<Void, List<VisibleLocation>> retrieveVisibleLocationsForActiveJourneyInteractor) {
        this.retrieveActiveJourneysInteractor = retrieveActiveJourneysInteractor;
        this.persistSingleJourneyInteractor = persistSingleJourneyInteractor;
        this.retrieveVisibleLocationsForActiveJourneyInteractor = retrieveVisibleLocationsForActiveJourneyInteractor;

        this.permissionsRequestLiveEvent = new SingleLiveEvent<>();
        this.permissionGrantLiveEvent = new SingleLiveEvent<>();
        this.trackingStateStreamEvent = new MutableLiveData<>();
        this.mapEventsObservableStream = new SingleLiveEvent<>();
        this.journeyEventsObservableStream = new MutableLiveData<>();

        this.compositeDisposable = new CompositeDisposable();

        this.compositeDisposable.add(this.retrieveActiveJourneysInteractor.retrieve(null)
                .subscribe(new ActiveJourneysUpdatesConsumer())
        );
    }

    public LiveData<PermissionsRequestEvent> getObservablePermissionRequestStream() {
        return this.permissionsRequestLiveEvent;
    }

    public LiveData<Void> getObservablePermissionGrantStream() {
        return this.permissionGrantLiveEvent;
    }

    public LiveData<Boolean> getObservableTrackingStateStream() {
        return this.trackingStateStreamEvent;
    }

    public LiveData<MapEvent> getObservableMapEventStream() {
        return this.mapEventsObservableStream;
    }

    public LiveData<PathEvent> getObservableJourneyEventStream() {
        return this.journeyEventsObservableStream;
    }


    public void requestPermissions(String... requiredPermissions) {
        Schedulers.computation().scheduleDirect(() -> {
            PermissionsRequestEvent permissionsRequestEvent = new PermissionsRequestEvent(requiredPermissions, R.string.permission_access_location_denied_rationale);
            MapViewModel.this.permissionsRequestLiveEvent.postValue(permissionsRequestEvent);
        });
    }

    public void onPermissionsGranted() {
        Schedulers.computation().scheduleDirect(() -> MapViewModel.this.permissionGrantLiveEvent.postValue(null));
    }

    public void onTrackingButtonClicked() {
        final Subscription[] subscription = {null};
        MapViewModel.this.retrieveActiveJourneysInteractor
                .retrieve(null)
                .observeOn(Schedulers.computation())
                .doOnSubscribe(s -> subscription[0] = s)
                .doOnNext(activeJourneys -> {
                    if (!activeJourneys.isEmpty()) {
                        VisibleJourney activeJourney = activeJourneys.get(0);
                        String completedDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(System.currentTimeMillis());
                        VisibleJourney completedJourney = new VisibleJourney(activeJourney.getIdentifier(), true, activeJourney.getStartedAtUTCDateTimeIso(), completedDateTimeUTCString, activeJourney.getTitle());
                        MapViewModel.this.persistSingleJourneyInteractor
                                .persist(null, completedJourney)
                                .observeOn(Schedulers.computation())
                                .doOnComplete(() -> MapViewModel.this.mapEventsObservableStream.postValue(new MapEvent(MapEvent.Type.TYPE_STOP_TRACKING_SERVICE, -1)))
                                .subscribe();
                    }
                    else {
                        MapViewModel.this.mapEventsObservableStream.postValue(new MapEvent(MapEvent.Type.TYPE_SHOW_NEW_JOURNEY_CREATOR, -1));
                    }
                    subscription[0].cancel();
                })
                .subscribe();
    }

    public void onJourneyCreationValuesSubmitted(@NonNull String journeyName) {
        String journeyStartDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(System.currentTimeMillis());
        String journeyEndDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(0);
        VisibleJourney activeJourney = new VisibleJourney(VisibleJourney.GENERATE_NEW_IDENTIFIER, false, journeyStartDateTimeUTCString, journeyEndDateTimeUTCString, journeyName);
        this.persistSingleJourneyInteractor
                .persist(null, activeJourney)
                .observeOn(Schedulers.computation())
                .doOnComplete(() -> {
                    final Subscription[] subscription = {null};
                    MapViewModel.this.retrieveActiveJourneysInteractor
                            .retrieve(null)
                            .observeOn(Schedulers.computation())
                            .doOnSubscribe(s -> subscription[0] = s)
                            .doOnNext(visibleJourneys -> {
                                if (!visibleJourneys.isEmpty()) {
                                    VisibleJourney activeJourney1 = visibleJourneys.get(0);
                                    MapEvent mapEvent = new MapEvent(MapEvent.Type.TYPE_START_TRACKING_SERVICE, activeJourney1.getIdentifier());
                                    MapViewModel.this.mapEventsObservableStream.postValue(mapEvent);
                                }
                                subscription[0].cancel();
                            })
                            .subscribe();

                })
                .subscribe();
    }

    private void clearActivePathUpdatesDisposableIfNeeded() {
        if (this.activePathUpdatesConsumerDisposable != null && !this.activePathUpdatesConsumerDisposable.isDisposed()) {
            this.activePathUpdatesConsumerDisposable.dispose();
        }
    }

    @Override
    protected void onCleared() {
        clearActivePathUpdatesDisposableIfNeeded();
        this.compositeDisposable.dispose();
        super.onCleared();
    }

    private class ActiveJourneysUpdatesConsumer implements Consumer<List<VisibleJourney>> {

        @Override
        public void accept(List<VisibleJourney> visibleJourneys) {
            if (visibleJourneys.isEmpty()) {
                MapViewModel.this.trackingStateStreamEvent.postValue(false);
                clearActivePathUpdatesDisposableIfNeeded();
            }
            else {
                MapViewModel.this.trackingStateStreamEvent.postValue(true);
                MapViewModel.this.activePathUpdatesConsumerDisposable = MapViewModel.this.retrieveVisibleLocationsForActiveJourneyInteractor
                        .retrieve(null)
                        .observeOn(Schedulers.computation())
                        .subscribe(new ActivePathRecordingUpdatesConsumer());
            }
        }
    }

    private class ActivePathRecordingUpdatesConsumer implements Consumer<List<VisibleLocation>> {

        @Override
        public void accept(List<VisibleLocation> visibleLocations) {
            List<LatLng> pathLatLngList = new ArrayList<>(visibleLocations.size());
            for (VisibleLocation visibleLocation : visibleLocations) {
                LatLng locationLatLng = new LatLng(visibleLocation.getLatitude(), visibleLocation.getLongitude());
                pathLatLngList.add(locationLatLng);
            }
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(pathLatLngList);
            PathEvent pathEvent = new PathEvent(PathEvent.Type.TYPE_PATH_UPDATE_RECEIVED, polylineOptions);
            MapViewModel.this.journeyEventsObservableStream.postValue(pathEvent);
        }
    }
}
