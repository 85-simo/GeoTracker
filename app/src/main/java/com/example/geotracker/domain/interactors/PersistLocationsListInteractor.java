package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleLocation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

class PersistLocationsListInteractor implements PersistInteractor<Long, List<VisibleLocation>> {
    @NonNull
    private Repository repository;

    @Inject
    public PersistLocationsListInteractor(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Completable persist(Long param, List<VisibleLocation> visibleLocationList) {
        List<RestrictedLocation> restrictedLocations = new ArrayList<>(visibleLocationList.size());
        for (VisibleLocation visibleLocation : visibleLocationList) {
            RestrictedLocation restrictedLocation = new RestrictedLocation(visibleLocation.getIdentifier(),
                    visibleLocation.getLatitude(),
                    visibleLocation.getLongitude(),
                    visibleLocation.getRecordedAtIso());
            restrictedLocations.add(restrictedLocation);
        }
        return this.repository
                .addLocationsToJourney(restrictedLocations, param);
    }
}
