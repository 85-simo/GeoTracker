package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Location;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class MappersModule {

    @Binds
    abstract Function<RestrictedLocation, Location> bindRestrictedToEntityLocationMapper(RestrictedToEntityLocationMapper mapper);

    @Binds
    abstract Function<Location, RestrictedLocation> bindEntityToRestrictedLocationMapper(EntityToRestrictedLocationMapper mapper);
}
