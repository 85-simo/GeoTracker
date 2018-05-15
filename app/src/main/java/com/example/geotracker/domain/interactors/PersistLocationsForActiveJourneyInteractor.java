package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

class PersistLocationsForActiveJourneyInteractor implements PersistInteractor<Void, List<VisibleLocation>> {
    @NonNull
    private PersistInteractor<Long, List<VisibleLocation>> visibleLocationsPersistInteractor;
    @NonNull
    private GetInteractor<Void, List<VisibleJourney>> getActiveJourneySingleInteractor;

    @Inject
    PersistLocationsForActiveJourneyInteractor(@NonNull PersistInteractor<Long, List<VisibleLocation>> visibleLocationsPersistInteractor,
                                               @NonNull @ActiveJourneys GetInteractor<Void, List<VisibleJourney>> getActiveJourneySingleInteractor) {
        this.visibleLocationsPersistInteractor = visibleLocationsPersistInteractor;
        this.getActiveJourneySingleInteractor = getActiveJourneySingleInteractor;
    }

    @Override
    public Completable persist(Void param, List<VisibleLocation> visibleLocations) {
        return this.getActiveJourneySingleInteractor
                .get(null)
                .flatMapObservable(Observable::fromIterable)
                .take(1)
                .flatMapCompletable(new Function<VisibleJourney, CompletableSource>() {
                    @Override
                    public CompletableSource apply(VisibleJourney visibleJourney) throws Exception {
                        return visibleLocationsPersistInteractor
                                .persist(visibleJourney.getIdentifier(), visibleLocations);
                    }
                });
    }
}
