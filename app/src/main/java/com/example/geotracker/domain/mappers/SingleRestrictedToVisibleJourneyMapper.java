package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.domain.dtos.VisibleJourney;

import javax.inject.Inject;

import io.reactivex.functions.Function;

class SingleRestrictedToVisibleJourneyMapper implements Function<RestrictedJourney, VisibleJourney> {

    @Inject
    SingleRestrictedToVisibleJourneyMapper() {

    }

    @Override
    public VisibleJourney apply(RestrictedJourney restrictedJourney) throws Exception {
        return new VisibleJourney(restrictedJourney.getIdentifier(),
                restrictedJourney.isComplete(),
                restrictedJourney.getStartedAtUTCDateTimeIso(),
                restrictedJourney.getCompletedAtUTCDateTimeIso(),
                restrictedJourney.getTitle());
    }
}
