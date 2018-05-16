package com.example.geotracker.presentation.journeys;

import android.arch.lifecycle.ViewModel;

import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.ViewModelKey;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelModule.class})
public abstract class JourneysViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(JourneysViewModel.class)
    @PerActivity
    abstract ViewModel bindJourneysViewModel(JourneysViewModel journeysViewModel);
}