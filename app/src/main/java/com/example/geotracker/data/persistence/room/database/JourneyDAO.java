package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.room.entities.Journey;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class JourneyDAO {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertJourney(@NonNull Journey journey);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertJourneys(@NonNull List<Journey> journeys);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateJourney(@NonNull Journey journey);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateJourneys(@NonNull List<Journey> journeys);

    @Delete
    public abstract int deleteJourney(@NonNull Journey journey);

    @Delete
    public abstract int deleteJourneys(@NonNull List<Journey> journeys);

    @Transaction
    public void upsertJourney(@NonNull Journey journey) {
        try {
            insertJourney(journey);
        }
        catch (SQLiteConstraintException e) {
            updateJourney(journey);
        }
    }

    @Query("SELECT * FROM journeys")
    public abstract Single<List<Journey>> getAllJourneysSingle();

    @Query("SELECT * FROM journeys ORDER BY started_at DESC")
    public abstract Flowable<List<Journey>> getRefreshingSortedJourneys();
}
