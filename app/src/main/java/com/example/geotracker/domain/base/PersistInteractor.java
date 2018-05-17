package com.example.geotracker.domain.base;

import io.reactivex.Completable;

/**
 * Interactor interface used for representing entity save operations. Calling classes only know when the invoked operation has completed but they're kept
 * unaware of success/failure states.
 * @param <Params> Parameter specific to the operation.
 * @param <Entity> The entity that needs to be persisted.
 */
public interface PersistInteractor<Params, Entity> {
    Completable persist(Params param, Entity entity);
}
