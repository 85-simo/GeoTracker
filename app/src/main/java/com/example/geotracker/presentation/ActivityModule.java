package com.example.geotracker.presentation;

import android.app.Activity;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ActivityModule {

    @Binds
    @PerActivity
    @ActivityContext
    abstract Context bindActivityContext(Activity activity);
}
