package com.example.geotracker.presentation.details;

import android.arch.lifecycle.ViewModel;

import com.example.geotracker.PerActivity;
import com.example.geotracker.presentation.ViewModelKey;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelModule.class})
public abstract class JourneyDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(JourneyDetailsViewModel.class)
    @PerActivity
    abstract ViewModel bindJourneyDetailsViewModel(JourneyDetailsViewModel journeyDetailsViewModel);
}
