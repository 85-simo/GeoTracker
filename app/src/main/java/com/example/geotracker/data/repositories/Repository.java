package com.example.geotracker.data.repositories;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Repository {
    Single<List<RestrictedJourney>> getJourneysOneShot();
    Flowable<List<RestrictedLocation>> getRefreshingLocationsForJourney(long journeyId);
    void addLocationToJourney(RestrictedLocation location, long journeyIdentifier) throws Exception;
}
