package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;
import com.example.geotracker.domain.interactors.qualifiers.AllJourneys;

import java.util.List;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class DomainMappersModule {

    @AllJourneys
    @Binds
    abstract Function<List<RestrictedJourney>, List<VisibleJourney>> bindRestrictedToVisibleJourneyMapper(RestrictedToVisibleJourneysMapper mapper);

    @Binds
    abstract Function<List<RestrictedLocation>, List<VisibleLocation>> bindRestrictedToVisibleLocationMapper(RestrictedToVisibleLocationsMapper mapper);

    @ActiveJourneys
    @Binds
    abstract Function<List<RestrictedJourney>, List<VisibleJourney>> bindRestrictedToVisibleJourneysFilteredMapper(RestrictedToVisibleJourneysFilteredMapper mapper);
}
