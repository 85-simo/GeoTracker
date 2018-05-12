package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.database.LocationDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
class RepositoryImpl implements Repository {
    @NonNull
    private JourneyDAO journeyDAO;
    @NonNull
    private LocationDAO locationDAO;
    @NonNull
    private Function<Location, RestrictedLocation> entityToRestrictedLocationMapper;
    @NonNull
    private Function<Journey, RestrictedJourney> entityToRestrictedJourneyMapper;

    @Inject
    RepositoryImpl(@NonNull JourneyDAO journeyDAO,
                   @NonNull LocationDAO locationDAO,
                   @NonNull Function<Location, RestrictedLocation> entityToRestrictedLocationMapper,
                   @NonNull Function<Journey, RestrictedJourney> entityToRestrictedJourneyMapper) {
        this.journeyDAO = journeyDAO;
        this.locationDAO = locationDAO;
        this.entityToRestrictedLocationMapper = entityToRestrictedLocationMapper;
        this.entityToRestrictedJourneyMapper = entityToRestrictedJourneyMapper;
    }


    @Override
    public Single<List<RestrictedJourney>> getJourneysOneShot() {
        return this.journeyDAO
                .getAllJourneysSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMapObservable(Observable::fromIterable)
                .map(this.entityToRestrictedJourneyMapper)
                .toList();
    }

    @Override
    public Flowable<List<RestrictedLocation>> getRefreshingLocationsForJourney(long journeyId) {
        return this.locationDAO
                .getSortedLocationsByJourneyIdFlowable(journeyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(Flowable::fromIterable)
                .map(this.entityToRestrictedLocationMapper)
                .toList()
                .toFlowable();
    }

    @Override
    public Completable addLocationToJourney(RestrictedLocation restrictedLocation, long journeyId) {
        return Completable.create(emitter -> {
            try {
                long recordAtMillis = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedLocation.getRecordedAtIso());
                Location location = new Location(restrictedLocation.getIdentifier(), restrictedLocation.getLatitude(), restrictedLocation.getLongitude(), recordAtMillis, journeyId);
                RepositoryImpl.this.locationDAO.upsertLocation(location);
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        })
       .subscribeOn(Schedulers.io())
       .observeOn(Schedulers.computation());
    }
}
