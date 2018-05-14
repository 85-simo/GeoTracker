package com.example.geotracker;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class GeoTrackerApplication extends Application implements HasActivityInjector, HasServiceInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeLeakCanary();
        initializeJodaTime();
        initializeDagger();
    }

    private void initializeJodaTime() {
        JodaTimeAndroid.init(this);
    }

    private void initializeLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initializeDagger() {
        DaggerAppComponent
                .builder()
                .application(this)
                .withDatabaseName(getString(R.string.database_name))
                .build()
                .inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return this.activityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return this.serviceInjector;
    }
}
