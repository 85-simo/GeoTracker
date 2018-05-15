package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

import java.util.List;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class DataMappersModule {

    @Binds
    abstract Function<List<Location>, List<RestrictedLocation>> bindEntityToRestrictedLocationMapper(EntityToRestrictedLocationsMapper mapper);

    @Binds
    abstract Function<List<Journey>, List<RestrictedJourney>> bindEntityToRestrictedJourneyMapper(EntityToRestrictedJourneysMapper mapper);
}
