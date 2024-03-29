package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Interactor meant to request a non-refreshing snapshot of the current set of persisted journeys, map such set to presentation-level entities,
 * filter-out all non-active journeys and forward results to the inner layer.
 */
class GetActiveJourneyInteractor implements GetInteractor<Void, List<VisibleJourney>> {
    @NonNull
    private Repository repository;
    @NonNull
    private Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper;

    @Inject
    GetActiveJourneyInteractor(@NonNull Repository repository,
                               @NonNull @ActiveJourneys Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper) {
        this.repository = repository;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Single<List<VisibleJourney>> get(Void param) {
        return this.repository
                .getJourneysOneShot()
                .map(this.journeyMapper);
    }
}
