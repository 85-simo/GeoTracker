package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;

import javax.inject.Inject;

import io.reactivex.Completable;

public class PersistSingleJourneyInteractor implements PersistInteractor<Void, VisibleJourney> {
    @NonNull
    private Repository repository;

    @Inject
    PersistSingleJourneyInteractor(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Completable persist(Void param, VisibleJourney visibleJourney) {
        RestrictedJourney restrictedJourney = new RestrictedJourney(visibleJourney.getIdentifier(),
                visibleJourney.isComplete(),
                visibleJourney.getStartedAtUTCDateTimeIso(),
                visibleJourney.getCompletedAtUTCDateTimeIso(),
                visibleJourney.getTitle());
        return this.repository
                .insertOrUpdateJourney(restrictedJourney);
    }
}
