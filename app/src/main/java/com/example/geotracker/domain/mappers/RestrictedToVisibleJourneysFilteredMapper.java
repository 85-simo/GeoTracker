package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.domain.dtos.VisibleJourney;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

class RestrictedToVisibleJourneysFilteredMapper implements Function<List<RestrictedJourney>, List<VisibleJourney>> {

    @Inject
    RestrictedToVisibleJourneysFilteredMapper() {

    }

    @Override
    public List<VisibleJourney> apply(List<RestrictedJourney> restrictedJourneys) throws Exception {
        List<VisibleJourney> result = new LinkedList<>();
        for (RestrictedJourney restrictedJourney : restrictedJourneys) {
            if (!restrictedJourney.isComplete()) {
                result.add(new VisibleJourney(restrictedJourney.getIdentifier(),
                        restrictedJourney.isComplete(),
                        restrictedJourney.getStartedAtUTCDateTimeIso(),
                        restrictedJourney.getCompletedAtUTCDateTimeIso(),
                        restrictedJourney.getTitle(),
                        restrictedJourney.getEncodedPath()));
            }
        }
        return result;
    }
}
