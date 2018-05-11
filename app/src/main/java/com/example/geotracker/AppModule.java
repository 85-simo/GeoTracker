package com.example.geotracker;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppModule {

    @Binds
    @Singleton
    @ApplicationContext
    abstract Context bindApplicationContext(Application application);
}
