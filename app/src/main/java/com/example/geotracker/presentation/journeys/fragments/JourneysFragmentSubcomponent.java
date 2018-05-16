package com.example.geotracker.presentation.journeys.fragments;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface JourneysFragmentSubcomponent extends AndroidInjector<JourneysFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<JourneysFragment> {}
}
