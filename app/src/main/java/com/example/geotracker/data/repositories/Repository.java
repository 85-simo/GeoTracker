package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

import java.util.List;

import io.reactivex.Flowable;

public interface Repository {
    Flowable<List<Journey>> getRefreshingJourneys();
    Flowable<List<Location>> getRefreshingLocationsForJourney(long journeyId);
}
