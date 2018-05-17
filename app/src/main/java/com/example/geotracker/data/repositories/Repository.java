package com.example.geotracker.data.repositories;

import com.example.geotracker.data.dtos.RestrictedJourney;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Repository {
    Single<List<RestrictedJourney>> getJourneysOneShot();
    Single<Boolean> getSingleTrackingState();
    Flowable<List<RestrictedJourney>> getRefreshingJourneys();
    Flowable<RestrictedJourney> getRefreshingJourneyById(long journeyId);
    Flowable<Boolean> getRefreshingTrackingState();
    Completable insertOrUpdateJourney(RestrictedJourney journey);
    Completable setTrackingState(boolean trackingState);
}
