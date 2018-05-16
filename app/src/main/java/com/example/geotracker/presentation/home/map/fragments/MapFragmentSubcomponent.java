package com.example.geotracker.presentation.home.map.fragments;

import com.example.geotracker.PerFragment;
import com.example.geotracker.presentation.home.map.fragments.MapFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
@Subcomponent
public interface MapFragmentSubcomponent extends AndroidInjector<MapFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapFragment> {}
}
