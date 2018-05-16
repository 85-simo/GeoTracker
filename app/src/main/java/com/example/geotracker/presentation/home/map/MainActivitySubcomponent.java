package com.example.geotracker.presentation.home.map;

import com.example.geotracker.PerActivity;
import com.example.geotracker.presentation.home.journeys.JourneysViewModel;
import com.example.geotracker.presentation.home.journeys.JourneysViewModelModule;
import com.example.geotracker.presentation.home.journeys.fragments.JourneysFragmentModule;
import com.example.geotracker.presentation.home.map.fragments.MapFragmentModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {MainViewModelModule.class, MapFragmentModule.class, JourneysViewModelModule.class, JourneysFragmentModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
