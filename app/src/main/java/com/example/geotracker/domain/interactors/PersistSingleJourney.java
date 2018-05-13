package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;

import io.reactivex.Completable;

public class PersistSingleJourney implements PersistInteractor<Void, VisibleJourney> {
    @NonNull
    private Repository repository;

    PersistSingleJourney(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Completable persist(Void param, VisibleJourney visibleJourney) {
        RestrictedJourney restrictedJourney = new RestrictedJourney(visibleJourney.getIdentifier(),
                visibleJourney.isComplete(),
                visibleJourney.getStartedAtUTCDateTimeIso(),
                visibleJourney.getCompletedAtUTCDateTimeIso());
        return this.repository
                .insertOrUpdateJourney(restrictedJourney);
    }
}
