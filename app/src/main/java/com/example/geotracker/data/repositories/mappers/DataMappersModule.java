package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class DataMappersModule {

    @Binds
    abstract Function<Location, RestrictedLocation> bindEntityToRestrictedLocationMapper(EntityToRestrictedLocationMapper mapper);

    @Binds
    abstract Function<Journey, RestrictedJourney> bindEntityToRestrictedJourneyMapper(EntityToRestrictedJourneyMapper mapper);
}
