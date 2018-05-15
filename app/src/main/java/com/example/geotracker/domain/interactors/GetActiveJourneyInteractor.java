package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
class GetActiveJourneyInteractor implements GetInteractor<Void, List<VisibleJourney>> {
    @NonNull
    Repository repository;
    @NonNull
    Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper;
    @NonNull
    Predicate<VisibleJourney> journeyFilterPredicate;

    @Inject
    GetActiveJourneyInteractor(@NonNull Repository repository,
                               @NonNull @ActiveJourneys Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper,
                               @NonNull Predicate<VisibleJourney> journeyFilterPredicate) {
        this.repository = repository;
        this.journeyMapper = journeyMapper;
        this.journeyFilterPredicate = journeyFilterPredicate;
    }

    @Override
    public Single<List<VisibleJourney>> get(Void param) {
        return this.repository
                .getJourneysOneShot()
                .map(this.journeyMapper);
    }
}
