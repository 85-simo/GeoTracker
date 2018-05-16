package com.example.geotracker.presentation;

import android.arch.lifecycle.ViewModelProvider;

import com.example.geotracker.PerActivity;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {
    @Binds
    @PerActivity
    abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelFactory viewModelFactory);
}
