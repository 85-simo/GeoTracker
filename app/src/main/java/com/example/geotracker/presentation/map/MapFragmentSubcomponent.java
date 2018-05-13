package com.example.geotracker.presentation.map;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface MapFragmentSubcomponent extends AndroidInjector<MapFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapFragment> {}
}
