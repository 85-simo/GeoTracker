package com.example.geotracker.data;

import com.example.geotracker.data.persistence.prefs.SharedPrefsModule;
import com.example.geotracker.data.persistence.room.database.DatabaseModule;
import com.example.geotracker.data.repositories.mappers.MappersModule;

import dagger.Module;

@Module(includes = {DatabaseModule.class, MappersModule.class, SharedPrefsModule.class})
public class DataModule {
}
