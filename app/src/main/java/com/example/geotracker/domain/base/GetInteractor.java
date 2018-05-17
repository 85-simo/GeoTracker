package com.example.geotracker.domain.base;

import io.reactivex.Single;

/**
 * Interactor interface representing a Get operation (a one shot operation that returns a single result and completes immediately after, may either
 * succeed or fail).
 * @param <Params> Parameter object specific to the operation
 * @param <Result> Expected result Type
 */
public interface GetInteractor<Params, Result> {
    Single<Result> get(Params param);
}
