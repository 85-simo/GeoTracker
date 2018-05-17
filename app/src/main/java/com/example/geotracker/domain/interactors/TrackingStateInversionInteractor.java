package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.domain.base.BooleanInversionInteractor;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;

import javax.inject.Inject;

import io.reactivex.Completable;
@Deprecated
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
