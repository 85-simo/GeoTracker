package com.example.geotracker.domain.base;

import io.reactivex.Completable;

public interface PersistInteractor<Params, Entity> {
    Completable persist(Params param, Entity entity);
}
