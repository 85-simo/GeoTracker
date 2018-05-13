package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.RetrieveInteractor;

import io.reactivex.Flowable;

class RetrieveTrackingState implements RetrieveInteractor<Void, Boolean> {
    @NonNull
    private Repository repository;

    RetrieveTrackingState(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<Boolean> retrieve(Void aVoid) {
        return this.repository
                .getRefreshingTrackingState();
    }
}