package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.prefs.SharedPreferencesProvider;
import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.database.LocationDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
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
    private Function<List<Location>, List<RestrictedLocation>> entityToRestrictedLocationMapper;
    @NonNull
    private Function<List<Journey>, List<RestrictedJourney>> entityToRestrictedJourneyMapper;
    @NonNull
    private Function<Journey, RestrictedJourney> singleEntityToRestrictedJourneyMapper;
    @NonNull
    private SharedPreferencesProvider sharedPreferencesProvider;

    @Inject
    RepositoryImpl(@NonNull JourneyDAO journeyDAO,
                   @NonNull LocationDAO locationDAO,
                   @NonNull SharedPreferencesProvider sharedPreferencesProvider,
                   @NonNull Function<List<Location>, List<RestrictedLocation>> entityToRestrictedLocationMapper,
                   @NonNull Function<List<Journey>, List<RestrictedJourney>> entityToRestrictedJourneyMapper,
                   @NonNull Function<Journey, RestrictedJourney> singleEntityToRestrictedJourneyMapper) {
        this.journeyDAO = journeyDAO;
        this.locationDAO = locationDAO;
        this.entityToRestrictedLocationMapper = entityToRestrictedLocationMapper;
        this.entityToRestrictedJourneyMapper = entityToRestrictedJourneyMapper;
        this.singleEntityToRestrictedJourneyMapper = singleEntityToRestrictedJourneyMapper;
        this.sharedPreferencesProvider = sharedPreferencesProvider;
    }


    @Override
    public Single<List<RestrictedJourney>> getJourneysOneShot() {
        return this.journeyDAO
                .getAllJourneysSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this.entityToRestrictedJourneyMapper);
    }

    @Override
    public Flowable<RestrictedJourney> getRefreshingJourneyById(long journeyId) {
        return this.journeyDAO
                .getRefreshingJourneyById(journeyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this.singleEntityToRestrictedJourneyMapper);
    }

    @Override
    public Single<Boolean> getSingleTrackingState() {
        return this.sharedPreferencesProvider
                .getSingleBooleanPrefValue(SharedPreferencesProvider.PREF_KEY_TRACKING_ACTIVE);
    }

    @Override
    public Flowable<List<RestrictedJourney>> getRefreshingJourneys() {
        return this.journeyDAO
                .getRefreshingSortedJourneys()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this.entityToRestrictedJourneyMapper);
    }

    @Override
    public Flowable<List<RestrictedLocation>> getRefreshingLocationsByJourneyId(long journeyId) {
        return this.locationDAO
                .getSortedLocationsByJourneyIdFlowable(journeyId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this.entityToRestrictedLocationMapper);
    }

    @Override
    public Flowable<Boolean> getRefreshingTrackingState() {
        return this.sharedPreferencesProvider
                .getRefreshingBooleanPrefValue(SharedPreferencesProvider.PREF_KEY_TRACKING_ACTIVE);
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

    @Override
    public Completable addLocationsToJourney(List<RestrictedLocation> locations, long journeyIdentifier) {
        return Completable.create(emitter -> {
            try {
                List<Location> locationsToBePersisted = new ArrayList<>(locations.size());
                for (RestrictedLocation restrictedLocation : locations) {
                    long recordAtMillis = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedLocation.getRecordedAtIso());
                    Location location = new Location(restrictedLocation.getIdentifier(), restrictedLocation.getLatitude(), restrictedLocation.getLongitude(), recordAtMillis, journeyIdentifier);
                    locationsToBePersisted.add(location);
                }
                this.locationDAO.upsertLocations(locationsToBePersisted);
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
    }

    @Override
    public Completable insertOrUpdateJourney(RestrictedJourney restrictedJourney) {
        return Completable.create(emitter -> {
            try {
                long startedAtTimestamp = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedJourney.getStartedAtUTCDateTimeIso());
                long completedAtTimestamp = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedJourney.getCompletedAtUTCDateTimeIso());
                Journey journey = new Journey(restrictedJourney.getIdentifier(),
                        restrictedJourney.isComplete(),
                        startedAtTimestamp,
                        completedAtTimestamp,
                        restrictedJourney.getTitle());
                RepositoryImpl.this.journeyDAO.upsertJourney(journey);
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation());
    }

    @Override
    public Completable setTrackingState(boolean trackingState) {
        return this.sharedPreferencesProvider
                .putBooleanPrefValue(SharedPreferencesProvider.PREF_KEY_TRACKING_ACTIVE, trackingState);
    }
}
