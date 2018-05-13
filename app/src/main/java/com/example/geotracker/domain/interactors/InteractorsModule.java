package com.example.geotracker.domain.interactors;

import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;

import java.util.List;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InteractorsModule {
    @Binds
    abstract PersistInteractor<Void, VisibleJourney> bindSingleJourneyPersistInteractor(PersistSingleJourneyInteractor persistSingleJourneyInteractor);

    @Binds
    abstract PersistInteractor<Long, VisibleLocation> bindSingleLocationPersistInteractor(PersistSingleLocationInteractor persistSingleLocationInteractor);

    @Binds
    abstract GetInteractor<Void, List<VisibleJourney>> bindAllJourneysGetInteractor(GetJourneysInteractor getJourneysInteractor);

    @Binds
    abstract RetrieveInteractor<Long, List<VisibleLocation>> bindAllLocationsForGivenJourneyRetrieveInteractor(RetrieveLocationsInteractor retrieveLocationsInteractor);

    @Binds
    abstract RetrieveInteractor<Void, Boolean> bindTrackingStateRetrieveInteractor(RetrieveTrackingStateInteractor retrieveTrackingStateInteractor);

    @Binds
    abstract RetrieveInteractor<Void, List<VisibleJourney>> bindAllJourneysRetrieveInteractor(RetrieveJourneysInteractor retrieveJourneysInteractor);
}
