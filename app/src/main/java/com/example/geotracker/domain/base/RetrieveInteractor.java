package com.example.geotracker.domain.base;

import io.reactivex.Flowable;

/**
 * Interactor class providing the same kind of logic of a {@link GetInteractor} except for the fact that it returns "live" auto-refreshing results:
 * classes invoking the retrieve method are notified of changes in the resultset in a reactive fashion.
 * @param <Params> Params specific to the retrieval operation
 * @param <Result> Expected results type
 */
public interface RetrieveInteractor<Params, Result> {
    Flowable<Result> retrieve(Params params);
}
