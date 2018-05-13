package com.example.geotracker.data;

import com.example.geotracker.data.persistence.prefs.SharedPrefsModule;
import com.example.geotracker.data.persistence.room.database.DatabaseModule;
import com.example.geotracker.data.repositories.mappers.DataMappersModule;

import dagger.Module;

@Module(includes = {DatabaseModule.class, DataMappersModule.class, SharedPrefsModule.class})
public class DataModule {
}
