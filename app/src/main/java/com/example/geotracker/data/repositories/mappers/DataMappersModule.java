package com.example.geotracker.data.repositories.mappers;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;

import java.util.List;

import dagger.Binds;
import dagger.Module;
import io.reactivex.functions.Function;

@Module
public abstract class DataMappersModule {
    @Binds
    abstract Function<List<Journey>, List<RestrictedJourney>> bindEntityToRestrictedJourneyMapper(@NonNull EntityToRestrictedJourneysMapper mapper);

    @Binds
    abstract Function<Journey, RestrictedJourney> bindSingleEntityToRestrictedJourneyMapper(@NonNull SingleEntityToRestrictedJourneyMapper mapper);
}
