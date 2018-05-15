package com.example.geotracker.data.repositories;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Repository {
    Single<List<RestrictedJourney>> getJourneysOneShot();
    Single<Boolean> getSingleTrackingState();
    Flowable<List<RestrictedJourney>> getRefreshingJourneys();
    Flowable<List<RestrictedLocation>> getRefreshingLocationsForJourney(long journeyId);
    Flowable<Boolean> getRefreshingTrackingState();
    Completable addLocationToJourney(RestrictedLocation location, long journeyIdentifier);
    Completable addLocationsToJourney(List<RestrictedLocation> locations, long journeyIdentifier);
    Completable insertOrUpdateJourney(RestrictedJourney journey);
    Completable setTrackingState(boolean trackingState);
}
