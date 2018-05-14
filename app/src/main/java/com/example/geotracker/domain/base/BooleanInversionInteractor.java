package com.example.geotracker.domain.base;

import io.reactivex.Completable;

public interface BooleanInversionInteractor<Key> {
    Completable invertBooleanValue(Key key);
}
