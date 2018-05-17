package com.example.geotracker.data.repositories;

import com.example.geotracker.data.dtos.RestrictedJourney;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Interface defining a class implementing the (simplified) Repository pattern: all domain-level classes are allowed to interact with
 * data-layer only through this interface, thus being (almost)totally decoupled and unaffected by future changes in datalayer-related logic.
 * This interface defines all operations supported by the underlying Repository's concrete implementation and as such limits interaction
 * to a subset of exposed methods avoiding the creation of direct dependencies between different layers.
 */
public interface Repository {
    Single<List<RestrictedJourney>> getJourneysOneShot();
    Single<Boolean> getSingleTrackingState();
    Flowable<List<RestrictedJourney>> getRefreshingJourneys();
    Flowable<RestrictedJourney> getRefreshingJourneyById(long journeyId);
    Flowable<Boolean> getRefreshingTrackingState();
    Completable insertOrUpdateJourney(RestrictedJourney journey);
    Completable setTrackingState(boolean trackingState);
}
