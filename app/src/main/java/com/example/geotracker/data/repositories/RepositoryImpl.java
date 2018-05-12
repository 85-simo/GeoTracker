package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.database.LocationDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Singleton
class RepositoryImpl implements Repository {
    @NonNull
    private JourneyDAO journeyDAO;
    @NonNull
    private LocationDAO locationDAO;
    @NonNull
    private Function<RestrictedLocation, Location> restrictedLocationToEntityMapper;
    @NonNull
    private Function<Location, RestrictedLocation> entityToRestrictedLocationMapper;
    @NonNull
    private Function<Journey, RestrictedJourney> entityToRestrictedJourneyMapper;

    @Inject
    RepositoryImpl(@NonNull JourneyDAO journeyDAO,
                   @NonNull LocationDAO locationDAO,
                   @NonNull Function<RestrictedLocation, Location> restrictedLocationToEntityMapper,
                   @NonNull Function<Location, RestrictedLocation> entityToRestrictedLocationMapper,
                   @NonNull Function<Journey, RestrictedJourney> entityToRestrictedJourneyMapper) {
        this.journeyDAO = journeyDAO;
        this.locationDAO = locationDAO;
        this.restrictedLocationToEntityMapper = restrictedLocationToEntityMapper;
        this.entityToRestrictedLocationMapper = entityToRestrictedLocationMapper;
        this.entityToRestrictedJourneyMapper = entityToRestrictedJourneyMapper;
    }


    @Override
    public Single<List<RestrictedJourney>> getJourneysOneShot() {
        return this.journeyDAO
                .getAllJourneysSingle()
                .flatMapObservable(Observable::fromIterable)
                .map(this.entityToRestrictedJourneyMapper)
                .toList();
    }

    @Override
    public Flowable<List<RestrictedLocation>> getRefreshingLocationsForJourney(long journeyId) {
        return this.locationDAO
                .getSortedLocationsByJourneyIdFlowable(journeyId)
                .flatMap(Flowable::fromIterable)
                .map(this.entityToRestrictedLocationMapper)
                .toList()
                .toFlowable();
    }

    @Override
    public void addLocationToJourney(RestrictedLocation location, long journeyId) throws Exception {
        Location mappedLocation = this.restrictedLocationToEntityMapper
                .apply(location);
        mappedLocation.setJourneyId(journeyId);
        this.locationDAO.upsertLocation(mappedLocation);
    }
}
