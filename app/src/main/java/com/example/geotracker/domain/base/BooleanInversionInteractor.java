package com.example.geotracker.domain.base;

import io.reactivex.Completable;

/**
 * Interactor interface representing an interface for requesting the inversion of a boolean value represented by the provided Key object.
 * Not used anymore.
 * @param <Key>
 */
@Deprecated
public interface BooleanInversionInteractor<Key> {
    Completable invertBooleanValue(Key key);
}
