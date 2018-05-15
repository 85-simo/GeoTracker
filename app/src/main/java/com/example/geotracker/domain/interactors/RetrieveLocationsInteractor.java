package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleLocation;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

class RetrieveLocationsInteractor implements RetrieveInteractor<Long, List<VisibleLocation>> {
    @NonNull
    private Repository repository;
    @NonNull
    private Function<List<RestrictedLocation>, List<VisibleLocation>> locationMapper;

    RetrieveLocationsInteractor(@NonNull Repository repository, @NonNull Function<List<RestrictedLocation>, List<VisibleLocation>> locationMapper) {
        this.repository = repository;
        this.locationMapper = locationMapper;
    }

    @Override
    public Flowable<List<VisibleLocation>> retrieve(@NonNull Long journeyId) {
        return this.repository
                .getRefreshingLocationsForJourney(journeyId)
                .map(this.locationMapper);
    }
}
