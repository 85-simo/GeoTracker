package com.example.geotracker.presentation.home.journeys.fragments;

import com.example.geotracker.PerFragment;
import com.example.geotracker.presentation.home.journeys.JourneysViewModelModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
@Subcomponent
public interface JourneysFragmentSubcomponent extends AndroidInjector<JourneysFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<JourneysFragment> {}
}
