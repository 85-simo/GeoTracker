package com.example.geotracker.presentation.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.geotracker.PerActivity;
import com.example.geotracker.R;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;
import com.example.geotracker.presentation.details.JourneyDetailsActivity;
import com.example.geotracker.presentation.home.map.events.ActivityEvent;
import com.example.geotracker.presentation.home.map.events.LocationUpdateEvent;
import com.example.geotracker.presentation.home.map.events.NavigationEvent;
import com.example.geotracker.presentation.home.map.events.PathEvent;
import com.example.geotracker.presentation.map.events.MapEvent;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.utils.DateTimeUtils;
import com.example.geotracker.utils.SingleLiveEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class MainViewModel extends ViewModel {
    @NonNull
    private RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor;
    @NonNull
    private PersistInteractor<Void, VisibleJourney> persistSingleJourneyInteractor;

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
    private MutableLiveData<ActivityEvent> activityEventsObservableStream;
    @NonNull
    private MutableLiveData<NavigationEvent> navigationEventsObservableStream;
    @NonNull
    private MutableLiveData<LocationUpdateEvent> locationUpdateEventsObservableStream;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @Inject
    MainViewModel(@NonNull @ActiveJourneys RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor,
                  @NonNull PersistInteractor<Void, VisibleJourney> persistSingleJourneyInteractor) {
        this.retrieveActiveJourneysInteractor = retrieveActiveJourneysInteractor;
        this.persistSingleJourneyInteractor = persistSingleJourneyInteractor;

        this.permissionsRequestLiveEvent = new SingleLiveEvent<>();
        this.permissionGrantLiveEvent = new SingleLiveEvent<>();
        this.trackingStateStreamEvent = new MutableLiveData<>();
        this.mapEventsObservableStream = new SingleLiveEvent<>();
        this.journeyEventsObservableStream = new MutableLiveData<>();
        this.activityEventsObservableStream = new SingleLiveEvent<>();
        this.navigationEventsObservableStream = new SingleLiveEvent<>();
        this.locationUpdateEventsObservableStream = new MutableLiveData<>();

        this.compositeDisposable = new CompositeDisposable();

        this.compositeDisposable.add(this.retrieveActiveJourneysInteractor.retrieve(null)
                .subscribe(new ActiveJourneysUpdatesConsumer())
        );
    }

    @NonNull
    public LiveData<PermissionsRequestEvent> getObservablePermissionRequestStream() {
        return this.permissionsRequestLiveEvent;
    }

    @NonNull
    public LiveData<Void> getObservablePermissionGrantStream() {
        return this.permissionGrantLiveEvent;
    }

    @NonNull
    public LiveData<Boolean> getObservableTrackingStateStream() {
        return this.trackingStateStreamEvent;
    }

    @NonNull
    public LiveData<MapEvent> getObservableMapEventStream() {
        return this.mapEventsObservableStream;
    }

    @NonNull
    public LiveData<PathEvent> getObservableJourneyEventStream() {
        return this.journeyEventsObservableStream;
    }

    @NonNull
    public LiveData<ActivityEvent> getObservableActivityEventStream() {
        return this.activityEventsObservableStream;
    }

    @NonNull
    public LiveData<NavigationEvent> getObservableNavigationEventsStream() {
        return this.navigationEventsObservableStream;
    }

    @NonNull
    public LiveData<LocationUpdateEvent> getObservableLocationUpdatesStream() {
        return this.locationUpdateEventsObservableStream;
    }

    public void requestPermissions(String... requiredPermissions) {
        Schedulers.computation().scheduleDirect(() -> {
            PermissionsRequestEvent permissionsRequestEvent = new PermissionsRequestEvent(requiredPermissions, R.string.permission_access_location_denied_rationale);
            MainViewModel.this.permissionsRequestLiveEvent.postValue(permissionsRequestEvent);
        });
    }

    public void onPermissionsGranted() {
        Schedulers.computation().scheduleDirect(() -> MainViewModel.this.permissionGrantLiveEvent.postValue(null));
    }

    public void onTabSelected(@IdRes int selectedTabId, boolean itemAlreadyChecked) {
        Schedulers.computation().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                if (!itemAlreadyChecked) {
                    ActivityEvent.TabType selectedTabType = null;
                    if (selectedTabId == R.id.item_map) {
                        selectedTabType = ActivityEvent.TabType.TAB_TYPE_MAP;
                    }
                    else if (selectedTabId == R.id.item_list) {
                        selectedTabType = ActivityEvent.TabType.TAB_TYPE_JOURNEYS;
                    }
                    if (selectedTabType != null) {
                        ActivityEvent activityEvent = new ActivityEvent(ActivityEvent.Type.TYPE_TAB_SELECTED, selectedTabType);
                        MainViewModel.this.activityEventsObservableStream.postValue(activityEvent);
                    }
                }
            }
        });
    }

    public void onJourneyItemClicked(long clickedJourneyId, boolean isActive) {
        Schedulers.computation().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                if (!isActive) {
                    Bundle extras = new Bundle();
                    extras.putLong(JourneyDetailsActivity.EXTRA_JOURNEY_ID, clickedJourneyId);
                    NavigationEvent navigationEvent = new NavigationEvent(NavigationEvent.Type.TYPE_ACTIVITY, JourneyDetailsActivity.class, extras);
                    MainViewModel.this.navigationEventsObservableStream.postValue(navigationEvent);
                }
                else {
                    ActivityEvent.TabType selectedTabType = ActivityEvent.TabType.TAB_TYPE_MAP;
                    ActivityEvent activityEvent = new ActivityEvent(ActivityEvent.Type.TYPE_TAB_SELECTED, selectedTabType);
                    MainViewModel.this.activityEventsObservableStream.postValue(activityEvent);
                }
            }
        });
    }

    public void onTrackingButtonClicked() {
        final Subscription[] subscription = {null};
        MainViewModel.this.retrieveActiveJourneysInteractor
                .retrieve(null)
                .observeOn(Schedulers.computation())
                .doOnSubscribe(s -> subscription[0] = s)
                .doOnNext(activeJourneys -> {
                    if (!activeJourneys.isEmpty()) {
                        VisibleJourney activeJourney = activeJourneys.get(0);
                        String completedDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(System.currentTimeMillis());
                        VisibleJourney completedJourney = new VisibleJourney(activeJourney.getIdentifier(), true, activeJourney.getStartedAtUTCDateTimeIso(), completedDateTimeUTCString, activeJourney.getTitle(), activeJourney.getEncodedPath());
                        MainViewModel.this.persistSingleJourneyInteractor
                                .persist(null, completedJourney)
                                .observeOn(Schedulers.computation())
                                .doOnComplete(() -> MainViewModel.this.mapEventsObservableStream.postValue(new MapEvent(MapEvent.Type.TYPE_STOP_TRACKING_SERVICE, -1)))
                                .subscribe();
                    }
                    else {
                        MainViewModel.this.mapEventsObservableStream.postValue(new MapEvent(MapEvent.Type.TYPE_SHOW_NEW_JOURNEY_CREATOR, -1));
                    }
                    subscription[0].cancel();
                })
                .subscribe();
    }

    public void onJourneyCreationValuesSubmitted(@NonNull String journeyName) {
        String journeyStartDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(System.currentTimeMillis());
        String journeyEndDateTimeUTCString = DateTimeUtils.utcMillisToDateTimeIsoString(0);
        VisibleJourney activeJourney = new VisibleJourney(VisibleJourney.GENERATE_NEW_IDENTIFIER, false, journeyStartDateTimeUTCString, journeyEndDateTimeUTCString, journeyName, null);
        this.persistSingleJourneyInteractor
                .persist(null, activeJourney)
                .observeOn(Schedulers.computation())
                .doOnComplete(() -> {
                    final Subscription[] subscription = {null};
                    MainViewModel.this.retrieveActiveJourneysInteractor
                            .retrieve(null)
                            .observeOn(Schedulers.computation())
                            .doOnSubscribe(s -> subscription[0] = s)
                            .doOnNext(visibleJourneys -> {
                                if (!visibleJourneys.isEmpty()) {
                                    VisibleJourney activeJourney1 = visibleJourneys.get(0);
                                    MapEvent mapEvent = new MapEvent(MapEvent.Type.TYPE_START_TRACKING_SERVICE, activeJourney1.getIdentifier());
                                    MainViewModel.this.mapEventsObservableStream.postValue(mapEvent);
                                }
                                subscription[0].cancel();
                            })
                            .subscribe();

                })
                .subscribe();
    }

    public void signalLocationUpdateReceived(double lat, double lng) {
        Schedulers.computation().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                LocationUpdateEvent locationUpdateEvent = new LocationUpdateEvent(lat, lng);
                MainViewModel.this.locationUpdateEventsObservableStream.postValue(locationUpdateEvent);
            }
        });
    }

    @Override
    protected void onCleared() {
        this.compositeDisposable.dispose();
        super.onCleared();
    }

    private class ActiveJourneysUpdatesConsumer implements Consumer<List<VisibleJourney>> {

        @Override
        public void accept(List<VisibleJourney> visibleJourneys) {
            if (visibleJourneys.isEmpty()) {
                MainViewModel.this.trackingStateStreamEvent.postValue(false);
            }
            else {
                MainViewModel.this.trackingStateStreamEvent.postValue(true);
                String encodedPath = visibleJourneys.get(0).getEncodedPath();
                if (!TextUtils.isEmpty(encodedPath)) {
                    List<LatLng> decodedPath = PolyUtil.decode(encodedPath);
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(decodedPath);
                    PathEvent pathEvent = new PathEvent(PathEvent.Type.TYPE_PATH_UPDATE_RECEIVED, polylineOptions);
                    MainViewModel.this.journeyEventsObservableStream.postValue(pathEvent);
                }
            }
        }
    }
}
