package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.geotracker.data.persistence.room.entities.Location;

import java.util.List;

@Dao
public abstract class LocationDAO {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertLocation(@NonNull Location location);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertLocations(@NonNull List<Location> locations);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateLocation(@NonNull Location location);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateLocations(@NonNull List<Location> locations);

    @Delete
    public abstract int deleteLocation(@NonNull Location location);

    @Delete
    public abstract int deleteLocations(@NonNull List<Location> locations);

    @Transaction
    public void upsertLocation(@NonNull Location location) {
        try {
            insertLocation(location);
        }
        catch (SQLiteConstraintException e) {
            updateLocation(location);
        }
    }
}
