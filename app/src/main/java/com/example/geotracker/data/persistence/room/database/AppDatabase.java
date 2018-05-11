package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.geotracker.BuildConfig;
import com.example.geotracker.data.persistence.room.database.type_converters.DateTimeTypeConverter;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.persistence.room.entities.Location;

@Database(entities = {Journey.class, Location.class},
        version = BuildConfig.DB_VERSION)
@TypeConverters({DateTimeTypeConverter.class})
abstract class AppDatabase extends RoomDatabase {
    public abstract JourneyDAO journeyDAO();
    public abstract LocationDAO locationDAO();
}
