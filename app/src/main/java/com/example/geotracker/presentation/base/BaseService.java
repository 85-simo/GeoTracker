package com.example.geotracker.presentation.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import dagger.android.AndroidInjection;

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }
}
