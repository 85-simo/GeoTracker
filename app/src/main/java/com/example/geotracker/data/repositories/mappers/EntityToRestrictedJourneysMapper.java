package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * Same as {@link EntityToRestrictedJourneysMapper} but applicable to {@link List} objects.
 */
public class EntityToRestrictedJourneysMapper implements Function<List<Journey>, List<RestrictedJourney>> {

    @Inject
    EntityToRestrictedJourneysMapper() {

    }

    @Override
    public List<RestrictedJourney> apply(List<Journey> journeys) throws Exception {
        List<RestrictedJourney> result = new ArrayList<>(journeys.size());
        for (Journey journey : journeys) {
            String startedAtDateTimeIso = DateTimeUtils.utcMillisToDateTimeIsoString(journey.getStartedAtTimestamp());
            String completedAtDateTimeIso = DateTimeUtils.utcMillisToDateTimeIsoString(journey.getCompletedAtTimestamp());
            result.add(new RestrictedJourney(journey.getId(), journey.isComplete(), startedAtDateTimeIso, completedAtDateTimeIso, journey.getTitle(), journey.getEncodedPath()));
        }
        return result;
    }
}
