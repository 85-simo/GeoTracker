package com.example.geotracker.presentation.tracking;

import android.app.Service;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {TrackingServiceSubcomponent.class})
public abstract class TrackingServiceModule {
    @Binds
    @IntoMap
    @ServiceKey(TrackingService.class)
    abstract AndroidInjector.Factory<? extends Service> bindMapActivityInjectorFactory(TrackingServiceSubcomponent.Builder builder);
}
