package com.example.geotracker.presentation.base;

import android.app.Service;

import dagger.android.AndroidInjection;

/**
 * Base {@link Service} class, used for centralising DI of services within the app.
 */
public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }
}
