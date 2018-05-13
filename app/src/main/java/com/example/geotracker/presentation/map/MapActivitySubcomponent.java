package com.example.geotracker.presentation.map;

import com.example.geotracker.presentation.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {MapViewModelModule.class})
public interface MapActivitySubcomponent extends AndroidInjector<MapActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapActivity> {}
}
