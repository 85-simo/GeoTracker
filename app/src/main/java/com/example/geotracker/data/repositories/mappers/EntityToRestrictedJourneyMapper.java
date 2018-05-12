package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;

import io.reactivex.functions.Function;

public class EntityToRestrictedJourneyMapper implements Function<Journey, RestrictedJourney> {

    @Override
    public RestrictedJourney apply(Journey journey) throws Exception {
        return new RestrictedJourney(journey.getId(), journey.isComplete());
    }
}
