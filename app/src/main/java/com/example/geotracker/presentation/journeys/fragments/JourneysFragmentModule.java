package com.example.geotracker.presentation.journeys.fragments;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {JourneysFragmentSubcomponent.class})
public abstract class JourneysFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(JourneysFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindJourneysFragmentInjectorFactory(JourneysFragmentSubcomponent.Builder builder);
}
