package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.prefs.SharedPreferencesProvider;
import com.example.geotracker.data.repositories.Repository;
import com.example.geotracker.domain.base.BooleanInversionInteractor;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

class TrackingStateInversionInteractor implements BooleanInversionInteractor<Void> {
    @NonNull
    private PersistInteractor<Void, Boolean> trackingStatePersistInteractor;
    @NonNull
    private GetInteractor<Void, Boolean> trackingStateGetInteractor;

    @Inject
    TrackingStateInversionInteractor(@NonNull PersistInteractor<Void, Boolean> trackingStatePersistInteractor, @NonNull GetInteractor<Void, Boolean> trackingStateGetInteractor) {
        this.trackingStateGetInteractor = trackingStateGetInteractor;
        this.trackingStatePersistInteractor = trackingStatePersistInteractor;
    }


    @Override
    public Completable invertBooleanValue(Void aVoid) {
        return this.trackingStateGetInteractor
                .get(null)
                .flatMapCompletable(trackingState -> TrackingStateInversionInteractor.this.trackingStatePersistInteractor
                        .persist(null, !trackingState));
    }
}
