package com.example.geotracker.domain.base;

import io.reactivex.Flowable;

public interface RetrieveInteractor<Params, Result> {
    Flowable<Result> retrieve(Params params);
}
