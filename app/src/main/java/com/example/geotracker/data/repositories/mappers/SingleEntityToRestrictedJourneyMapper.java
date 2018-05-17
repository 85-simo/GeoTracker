package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.utils.DateTimeUtils;

import javax.inject.Inject;

import io.reactivex.functions.Function;
/**
 * Mapper class using for defining conversion rules between a data-layer specific data-model class and its domain-specific equivalent representation.
 * It's kept separated from other classes in order to keep other components unaware of data-conversion (and implicitly of data-representation) logic.
 */
class SingleEntityToRestrictedJourneyMapper implements Function<Journey, RestrictedJourney> {

    @Inject
    SingleEntityToRestrictedJourneyMapper() {

    }

    @Override
    public RestrictedJourney apply(Journey journey) throws Exception {
        String startedAtDateTimeIso = DateTimeUtils.utcMillisToDateTimeIsoString(journey.getStartedAtTimestamp());
        String completedAtDateTimeIso = DateTimeUtils.utcMillisToDateTimeIsoString(journey.getCompletedAtTimestamp());
        return new RestrictedJourney(journey.getId(), journey.isComplete(), startedAtDateTimeIso, completedAtDateTimeIso, journey.getTitle(), journey.getEncodedPath());
    }
}
