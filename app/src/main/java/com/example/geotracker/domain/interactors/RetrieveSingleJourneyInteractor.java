package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class RetrieveSingleJourneyInteractor implements RetrieveInteractor<Long, VisibleJourney> {
    @NonNull
    private Repository repository;
    @NonNull
    private Function<RestrictedJourney, VisibleJourney> restrictedToVisibleJourneyMapper;

    @Inject
    public RetrieveSingleJourneyInteractor(@NonNull Repository repository, @NonNull Function<RestrictedJourney, VisibleJourney> restrictedToVisibleJourneyMapper) {
        this.repository = repository;
        this.restrictedToVisibleJourneyMapper = restrictedToVisibleJourneyMapper;
    }

    @Override
    public Flowable<VisibleJourney> retrieve(Long journeyId) {
        return this.repository
                .getRefreshingJourneyById(journeyId)
                .observeOn(Schedulers.computation())
                .map(this.restrictedToVisibleJourneyMapper);
    }
}
