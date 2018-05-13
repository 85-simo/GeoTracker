package com.example.geotracker.presentation.map;

import com.example.geotracker.presentation.ActivityModule;
import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {ActivityModule.class, MapViewModelModule.class, ViewModelModule.class})
public interface MapActivitySubcomponent extends AndroidInjector<MapActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapActivity> {}
}
