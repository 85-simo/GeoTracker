package com.example.geotracker.presentation.map;

import android.arch.lifecycle.ViewModel;

import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.ViewModelKey;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MapViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    @PerActivity
    abstract ViewModel bindMapViewModel(MapViewModel mapViewModel);
}
