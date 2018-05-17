package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.domain.dtos.VisibleJourney;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Same logic behind {@link com.example.geotracker.data.repositories.mappers.EntityToRestrictedJourneysMapper}, applied to entity conversions for communication
 * between the domain and presentation layers. In addition to that, this class applies a filter during the object conversion in order to include
 * the currently active journey only into the set of mapped results.
 */
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
