package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.database.LocationDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
class RepositoryImpl implements Repository {
    @NonNull
    private JourneyDAO journeyDAO;
    @NonNull
    private LocationDAO locationDAO;

    @Inject
    RepositoryImpl(@NonNull JourneyDAO journeyDAO, @NonNull LocationDAO locationDAO) {
        this.journeyDAO = journeyDAO;
        this.locationDAO = locationDAO;
    }

    @Override
    public Flowable<List<Journey>> getRefreshingJourneys() {
        return null;
    }

    @Override
    public Flowable<List<Location>> getRefreshingLocationsForJourney(long journeyId) {
        return null;
    }
}
