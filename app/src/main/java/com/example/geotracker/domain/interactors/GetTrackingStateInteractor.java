package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.GetInteractor;

import javax.inject.Inject;

import io.reactivex.Single;

class GetTrackingStateInteractor implements GetInteractor<Void, Boolean> {
    @NonNull
    private Repository repository;

    @Inject
    GetTrackingStateInteractor(@NonNull Repository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Boolean> get(Void param) {
        return this.repository
                .getSingleTrackingState();
    }
}
