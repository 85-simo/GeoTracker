package com.example.geotracker.presentation.base;

import android.app.Service;

import dagger.android.AndroidInjection;

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }
}
