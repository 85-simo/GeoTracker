package com.example.geotracker.data.repositories;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Repository {
    Single<List<RestrictedJourney>> getJourneysOneShot();
    Flowable<List<RestrictedLocation>> getRefreshingLocationsForJourney(long journeyId);
    Completable addLocationToJourney(RestrictedLocation location, long journeyIdentifier) throws Exception;
}
