package com.example.geotracker.data.persistence.prefs;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SharedPrefsModule {
    @Binds
    abstract SharedPreferencesProvider bindSharedPreferencesProvider(MainPrefsProvider mainPrefsProvider);
}
