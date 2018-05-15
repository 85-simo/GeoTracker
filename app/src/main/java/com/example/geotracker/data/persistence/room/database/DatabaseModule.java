package com.example.geotracker.data.persistence.room.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.geotracker.ApplicationContext;
import com.example.geotracker.DatabaseName;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    static AppDatabase provideAppDatabase(@ApplicationContext Context applicationContext, @DatabaseName String databaseName) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, databaseName)
                .build();
    }
    @Provides
    @Singleton
    static JourneyDAO provideJourneyDAO(@NonNull AppDatabase appDatabase) {
        return appDatabase.journeyDAO();
    }

    @Provides
    @Singleton
    static LocationDAO provideLocationDAO(@NonNull AppDatabase appDatabase) {
        return appDatabase.locationDAO();
    }
}
