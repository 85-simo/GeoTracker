package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

class RetrieveActiveJourneysInteractor implements RetrieveInteractor<Void, List<VisibleJourney>> {
    @NonNull
    private Repository repository;
    @NonNull
    private Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper;

    @Inject
    public RetrieveActiveJourneysInteractor(@NonNull Repository repository, @NonNull @ActiveJourneys Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper) {
        this.repository = repository;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public Flowable<List<VisibleJourney>> retrieve(Void aVoid) {
        return this.repository
                .getRefreshingJourneys()
                .observeOn(Schedulers.computation())
                .map(this.journeyMapper);
    }
}
