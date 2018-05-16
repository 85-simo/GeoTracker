package com.example.geotracker.presentation.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.geotracker.PerActivity;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.presentation.details.events.JourneyDetailsInfoEvent;
import com.example.geotracker.presentation.details.events.JourneyDetailsPathEvent;
import com.example.geotracker.utils.DateTimeUtils;
import com.example.geotracker.utils.DistanceUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class JourneyDetailsViewModel extends ViewModel {
    @NonNull
    private RetrieveInteractor<Long, VisibleJourney> journeyRetrieveInteractor;
    @NonNull
    private RetrieveInteractor<Long, List<VisibleLocation>> locationsRetrieveInteractor;

    @NonNull
    private MutableLiveData<JourneyDetailsInfoEvent> selectedJourneyObservableStream;
    @NonNull
    private MutableLiveData<JourneyDetailsPathEvent> journeyLocationsObervableStream;

    private Disposable journeyRetrievalDisposable;
    private Disposable locationsRetrievalDisposable;

    @Inject
    JourneyDetailsViewModel(@NonNull RetrieveInteractor<Long, VisibleJourney> journeyRetrieveInteractor, @NonNull RetrieveInteractor<Long, List<VisibleLocation>> locationsRetrieveInteractor) {
        this.journeyRetrieveInteractor = journeyRetrieveInteractor;
        this.locationsRetrieveInteractor = locationsRetrieveInteractor;

        this.selectedJourneyObservableStream = new MutableLiveData<>();
        this.journeyLocationsObervableStream = new MutableLiveData<>();
    }

    public void signalSelectedJourneyId(long selectedJourneyId) {
        if (this.journeyRetrievalDisposable != null && !this.journeyRetrievalDisposable.isDisposed()) {
            this.journeyRetrievalDisposable.dispose();
        }
        if (this.locationsRetrievalDisposable != null && !this.locationsRetrievalDisposable.isDisposed()) {
            this.locationsRetrievalDisposable.dispose();
        }
        this.journeyRetrievalDisposable = this.journeyRetrieveInteractor
                .retrieve(selectedJourneyId)
                .observeOn(Schedulers.computation())
                .zipWith(this.locationsRetrieveInteractor
                        .retrieve(selectedJourneyId)
                        .observeOn(Schedulers.computation()), (visibleJourney, visibleLocations) -> {
                            List<LatLng> pathSequence = new ArrayList<>(visibleLocations.size());
                            for (VisibleLocation visibleLocation : visibleLocations) {
                                pathSequence.add(new LatLng(visibleLocation.getLatitude(), visibleLocation.getLongitude()));
                            }
                            double totalDistance = DistanceUtils.computeDistance(pathSequence);
                            String startedAtHumanReadable = DateTimeUtils.isoUTCDateTimeStringToHumanReadable(visibleJourney.getStartedAtUTCDateTimeIso());
                            String completedAtHumanReadable = DateTimeUtils.isoUTCDateTimeStringToHumanReadable(visibleJourney.getCompletedAtUTCDateTimeIso());
                            long pathDuration = DateTimeUtils.millisBetween(visibleJourney.getStartedAtUTCDateTimeIso(), visibleJourney.getCompletedAtUTCDateTimeIso());
                            return new JourneyDetailsInfoEvent(visibleJourney.getTitle(), startedAtHumanReadable, completedAtHumanReadable, pathDuration, totalDistance);
                        })
                .subscribe(journeyDetailsInfoEvent -> JourneyDetailsViewModel.this.selectedJourneyObservableStream
                        .postValue(journeyDetailsInfoEvent));

        this.locationsRetrievalDisposable = this.locationsRetrieveInteractor
                .retrieve(selectedJourneyId)
                .observeOn(Schedulers.computation())
                .subscribe(visibleLocations -> {
                    List<LatLng> pathSequence = new ArrayList<>(visibleLocations.size());
                    LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
                    for (VisibleLocation visibleLocation : visibleLocations) {
                        LatLng latLng = new LatLng(visibleLocation.getLatitude(), visibleLocation.getLongitude());
                        pathSequence.add(latLng);
                        latLngBoundsBuilder.include(latLng);
                    }
                    LatLngBounds boundingBox = latLngBoundsBuilder.build();
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(pathSequence);
                    JourneyDetailsPathEvent journeyDetailsPathEvent = new JourneyDetailsPathEvent(polylineOptions, boundingBox);
                    JourneyDetailsViewModel.this.journeyLocationsObervableStream
                            .postValue(journeyDetailsPathEvent);
                });
    }

    public LiveData<JourneyDetailsInfoEvent> getJourneyInfoObservableStream() {
        return this.selectedJourneyObservableStream;
    }

    @NonNull
    public MutableLiveData<JourneyDetailsPathEvent> getJourneyPathObervableStream() {
        return journeyLocationsObervableStream;
    }

    @Override
    protected void onCleared() {
        if (this.journeyRetrievalDisposable != null && !this.journeyRetrievalDisposable.isDisposed()) {
            this.journeyRetrievalDisposable.dispose();
        }
        if (this.locationsRetrievalDisposable != null && !this.locationsRetrievalDisposable.isDisposed()) {
            this.locationsRetrievalDisposable.dispose();
        }
        super.onCleared();
    }
}
