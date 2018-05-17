package com.example.geotracker.data.repositories;

import android.support.annotation.NonNull;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.prefs.SharedPreferencesProvider;
import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Concerete implementation of the {@link Repository} interface:
 * It uses RxJava 2 for easy implementation of data operations/manipulations. All needed mappers and DAOs are injected through Dagger2 and references are only
 * kept through abstractions/interfaces. In this manner, the class is kept isolated from future changes occurring to all needed classes.
 * Same applies to this class as well: it's been given package-level visibility only in order to restrict unsafe instantiation: all classes
 * needing to interact with the data layer may do so only through the {@link Repository} interface.
 */
@Singleton
class RepositoryImpl implements Repository {
    @NonNull
    private JourneyDAO journeyDAO;
    @NonNull
    private Function<List<Journey>, List<RestrictedJourney>> entityToRestrictedJourneyMapper;
    @NonNull
    private Function<Journey, RestrictedJourney> singleEntityToRestrictedJourneyMapper;
    @NonNull
    private SharedPreferencesProvider sharedPreferencesProvider;

    @Inject
    RepositoryImpl(@NonNull JourneyDAO journeyDAO,
                   @NonNull SharedPreferencesProvider sharedPreferencesProvider,
                   @NonNull Function<List<Journey>, List<RestrictedJourney>> entityToRestrictedJourneyMapper,
                   @NonNull Function<Journey, RestrictedJourney> singleEntityToRestrictedJourneyMapper) {
        this.journeyDAO = journeyDAO;
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
    public Flowable<Boolean> getRefreshingTrackingState() {
        return this.sharedPreferencesProvider
                .getRefreshingBooleanPrefValue(SharedPreferencesProvider.PREF_KEY_TRACKING_ACTIVE);
    }

    @Override
    public Completable insertOrUpdateJourney(RestrictedJourney restrictedJourney) {
        return Completable.create(emitter -> {
            try {
                long startedAtTimestamp = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedJourney.getStartedAtUTCDateTimeIso());
                long completedAtTimestamp = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedJourney.getCompletedAtUTCDateTimeIso());
                Journey journey = new Journey(restrictedJourney.getIdentifier(),
                        restrictedJourney.isComplete(),
                        restrictedJourney.getTitle(),
                        startedAtTimestamp,
                        completedAtTimestamp,
                        restrictedJourney.getEncodedPath());
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
