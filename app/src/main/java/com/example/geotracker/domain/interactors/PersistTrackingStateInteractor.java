package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.PersistInteractor;

import javax.inject.Inject;

import io.reactivex.Completable;

public class PersistTrackingStateInteractor implements PersistInteractor<Void, Boolean> {
    @NonNull
    private Repository repository;

    @Inject
    PersistTrackingStateInteractor(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Completable persist(Void param, @NonNull Boolean trackingState) {
        return this.repository
                .setTrackingState(trackingState);
    }
}
