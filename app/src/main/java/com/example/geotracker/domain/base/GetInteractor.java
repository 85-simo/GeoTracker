package com.example.geotracker.domain.base;

import io.reactivex.Single;

public interface GetInteractor<Params, Result> {
    Single<Result> get(Params param);
}
