package com.example.geotracker.presentation.map;

import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.journeys.JourneysViewModelModule;
import com.example.geotracker.presentation.journeys.fragments.JourneysFragmentModule;
import com.example.geotracker.presentation.map.fragments.MapFragmentModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {MapViewModelModule.class, MapFragmentModule.class, JourneysViewModelModule.class, JourneysFragmentModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
