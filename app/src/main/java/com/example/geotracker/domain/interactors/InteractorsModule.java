package com.example.geotracker.domain.interactors;

import com.example.geotracker.domain.base.BooleanInversionInteractor;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;
import com.example.geotracker.domain.interactors.qualifiers.AllJourneys;

import java.util.List;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InteractorsModule {
    @Binds
    abstract PersistInteractor<Void, VisibleJourney> bindSingleJourneyPersistInteractor(PersistSingleJourneyInteractor persistSingleJourneyInteractor);

    @Binds
    abstract PersistInteractor<Void, Boolean> bindTrackingStatePersistInteractor(PersistTrackingStateInteractor persistTrackingStateInteractor);

    @Binds
    abstract GetInteractor<Void, List<VisibleJourney>> bindAllJourneysGetInteractor(GetJourneysInteractor getJourneysInteractor);

    @AllJourneys
    @Binds
    abstract GetInteractor<Void, Boolean> bindTrackingStateGetInteractor(GetTrackingStateInteractor getTrackingStateInteractor);

    @ActiveJourneys
    @Binds
    abstract GetInteractor<Void, List<VisibleJourney>> bindActiveJourneysGetInteractor(GetActiveJourneyInteractor getActiveJourneyInteractor);

    @Binds
    abstract RetrieveInteractor<Void, Boolean> bindTrackingStateRetrieveInteractor(RetrieveTrackingStateInteractor retrieveTrackingStateInteractor);

    @AllJourneys
    @Binds
    abstract RetrieveInteractor<Void, List<VisibleJourney>> bindAllJourneysRetrieveInteractor(RetrieveJourneysInteractor retrieveJourneysInteractor);

    @ActiveJourneys
    @Binds
    abstract RetrieveInteractor<Void, List<VisibleJourney>> bindActiveJourneysRetrieveInteractor(RetrieveActiveJourneysInteractor retrieveActiveJourneysInteractor);

    @Binds
    abstract RetrieveInteractor<Long, VisibleJourney> bindSingleJourneyRetrieveInteractor(RetrieveSingleJourneyInteractor retrieveSingleJourneyInteractor);

    @Binds
    abstract BooleanInversionInteractor<Void> bindTrackingStateInversionInteractor(TrackingStateInversionInteractor trackingStateInversionInteractor);
}
