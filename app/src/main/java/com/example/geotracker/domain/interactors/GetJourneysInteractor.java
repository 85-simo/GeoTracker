package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.AllJourneys;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

class GetJourneysInteractor implements GetInteractor<Void, List<VisibleJourney>> {
    @NonNull
    private Repository repository;
    @NonNull
    private Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper;

    GetJourneysInteractor(@NonNull Repository repository, @NonNull @AllJourneys Function<List<RestrictedJourney>, List<VisibleJourney>> journeyMapper) {
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
