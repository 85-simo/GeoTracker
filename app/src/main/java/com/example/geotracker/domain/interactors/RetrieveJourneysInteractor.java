package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.AllJourneys;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

class RetrieveJourneysInteractor implements RetrieveInteractor<Void, List<VisibleJourney>> {
    private Repository repository;
    private Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper;

    @Inject
    RetrieveJourneysInteractor(@NonNull Repository repository, @NonNull @AllJourneys Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper) {
        this.repository = repository;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Flowable<List<VisibleJourney>> retrieve(Void aVoid) {
        return this.repository
                .getRefreshingJourneys()
                .map(this.journeyMapper);
    }
}
