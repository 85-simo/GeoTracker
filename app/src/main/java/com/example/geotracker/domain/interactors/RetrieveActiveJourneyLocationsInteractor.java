package com.example.geotracker.domain.interactors;

import android.support.annotation.NonNull;

import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;

import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

class RetrieveActiveJourneyLocationsInteractor implements RetrieveInteractor<Void, List<VisibleLocation>> {
    @NonNull
    private RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor;
    @NonNull
    private RetrieveInteractor<Long, List<VisibleLocation>> retrieveLocationsForJourneyInteractor;

    @Inject
    RetrieveActiveJourneyLocationsInteractor(@NonNull @ActiveJourneys RetrieveInteractor<Void, List<VisibleJourney>> retrieveActiveJourneysInteractor,
                                             @NonNull RetrieveInteractor<Long, List<VisibleLocation>> retrieveLocationsForJourneyInteractor) {
        this.retrieveActiveJourneysInteractor = retrieveActiveJourneysInteractor;
        this.retrieveLocationsForJourneyInteractor = retrieveLocationsForJourneyInteractor;
    }
    @Override
    public Flowable<List<VisibleLocation>> retrieve(Void aVoid) {
        return this.retrieveActiveJourneysInteractor
                .retrieve(null)
                .flatMap(Flowable::fromIterable)
                .take(1)
                .flatMap((Function<VisibleJourney, Publisher<List<VisibleLocation>>>) visibleJourney -> retrieveLocationsForJourneyInteractor
                        .retrieve(visibleJourney.getIdentifier()));
    }
}
