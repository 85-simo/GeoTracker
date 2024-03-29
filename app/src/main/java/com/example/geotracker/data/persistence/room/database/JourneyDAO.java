package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.room.entities.Journey;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * A {@link Dao} class representing all needed interaction between an inner component
 * (a {@link com.example.geotracker.data.repositories.Repository}
 * component in this case) and the underlying data storage engine. Concrete implementation is generated at compile-time by
 * {@link android.arch.persistence.room.Room} annotation processor.
 *
 */
@Dao
public abstract class JourneyDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertJourney(@NonNull Journey journey);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertJourneys(@NonNull List<Journey> journeys);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract int updateJourney(@NonNull Journey journey);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateJourneys(@NonNull List<Journey> journeys);

    @Delete
    public abstract int deleteJourney(@NonNull Journey journey);

    @Delete
    public abstract int deleteJourneys(@NonNull List<Journey> journeys);

    @Transaction
    public void upsertJourney(@NonNull Journey journey) {
        Journey journeyById = getJourneyByIdSync(journey.getId());
        if (journeyById != null) {
            updateJourney(journey);
        }
        else {
            insertJourney(journey);
        }
    }

    @Query("SELECT * FROM journeys")
    public abstract Single<List<Journey>> getAllJourneysSingle();

    @Query("SELECT * FROM journeys ORDER BY started_at DESC")
    public abstract Flowable<List<Journey>> getRefreshingSortedJourneys();

    @Query("SELECT * FROM journeys WHERE id = :id")
    public abstract Journey getJourneyByIdSync(long id);

    @Query("SELECT * FROM journeys WHERE id = :id")
    public abstract Flowable<Journey> getRefreshingJourneyById(long id);
}
