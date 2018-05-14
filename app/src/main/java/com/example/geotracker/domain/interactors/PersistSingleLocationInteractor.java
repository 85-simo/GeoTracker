package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleLocation;

import javax.inject.Inject;

import io.reactivex.Completable;

class PersistSingleLocationInteractor implements PersistInteractor<Long, VisibleLocation> {
    @NonNull
    private Repository repository;

    @Inject
    PersistSingleLocationInteractor(@NonNull Repository repository) {
        this.repository = repository;
    }


    @Override
    public Completable persist(@NonNull Long journeyId, VisibleLocation visibleLocation) {
        RestrictedLocation restrictedLocation = new RestrictedLocation(visibleLocation.getIdentifier(),
                visibleLocation.getLatitude(),
                visibleLocation.getLongitude(),
                visibleLocation.getRecordedAtIso());
        return this.repository
                .addLocationToJourney(restrictedLocation, journeyId);
    }
}
