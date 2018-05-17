package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.domain.dtos.VisibleJourney;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Same as {@link RestrictedToVisibleJourneysFilteredMapper}, but does not apply any filter to the set of result: it maps all
 * objects it's given in input.
 */
class RestrictedToVisibleJourneysMapper implements Function<List<RestrictedJourney>, List<VisibleJourney>> {
    @Inject
    RestrictedToVisibleJourneysMapper() {

    }

    @Override
    public List<VisibleJourney> apply(List<RestrictedJourney> restrictedJourneys) throws Exception {
        List<VisibleJourney> result = new ArrayList<>(restrictedJourneys.size());
        for (RestrictedJourney restrictedJourney : restrictedJourneys) {
            result.add(new VisibleJourney(restrictedJourney.getIdentifier(),
                    restrictedJourney.isComplete(),
                    restrictedJourney.getStartedAtUTCDateTimeIso(),
                    restrictedJourney.getCompletedAtUTCDateTimeIso(),
                    restrictedJourney.getTitle(),
                    restrictedJourney.getEncodedPath()));
        }
        return result;
    }
}
