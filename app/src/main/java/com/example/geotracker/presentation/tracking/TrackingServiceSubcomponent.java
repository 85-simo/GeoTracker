package com.example.geotracker.presentation.tracking;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface TrackingServiceSubcomponent extends AndroidInjector<TrackingService> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TrackingService> {}
}
