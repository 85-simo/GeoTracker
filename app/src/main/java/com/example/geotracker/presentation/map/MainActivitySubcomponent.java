package com.example.geotracker.presentation.map;

import com.example.geotracker.presentation.ActivityModule;
import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = {MapViewModelModule.class, MapFragmentModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
