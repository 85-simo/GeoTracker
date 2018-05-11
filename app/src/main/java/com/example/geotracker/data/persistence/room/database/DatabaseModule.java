package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.geotracker.ApplicationContext;
import com.example.geotracker.DatabaseName;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class DatabaseModule {

    @Provides
    @Reusable
    static AppDatabase provideAppDatabase(@ApplicationContext Context applicationContext, @DatabaseName String databaseName) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, databaseName)
                .build();
    }
    @Provides
    @Reusable
    static JourneyDAO provideJourneyDAO(@NonNull AppDatabase appDatabase) {
        return appDatabase.journeyDAO();
    }

    @Provides
    @Reusable
    static LocationDAO provideLocationDAO(@NonNull AppDatabase appDatabase) {
        return appDatabase.locationDAO();
    }
}
