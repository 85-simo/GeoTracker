package com.example.geotracker.presentation.home.map.fragments;

import android.support.v4.app.Fragment;

import com.example.geotracker.presentation.home.map.fragments.MapFragment;
import com.example.geotracker.presentation.home.map.fragments.MapFragmentSubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {MapFragmentSubcomponent.class})
public abstract class MapFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MapFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMapFragmentInjectorFactory(MapFragmentSubcomponent.Builder builder);
}
