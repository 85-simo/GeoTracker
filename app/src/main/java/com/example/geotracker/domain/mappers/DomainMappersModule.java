package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class DomainMappersModule {

    @Binds
    abstract Function<RestrictedJourney, VisibleJourney> bindRestrictedToVisibleJourneyMapper(RestrictedToVisibleJourneyMapper mapper);

    @Binds
    abstract Function<RestrictedLocation, VisibleLocation> bindRestrictedToVisibleLocationMapper(RestrictedToVisibleLocationMapper mapper);
}
