package com.example.geotracker.presentation.home.map.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
@Subcomponent
public interface MapFragmentSubcomponent extends AndroidInjector<MapFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapFragment> {}
}
