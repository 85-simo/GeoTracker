package com.example.geotracker.presentation.home.map;

import android.arch.lifecycle.ViewModel;

import com.example.geotracker.PerActivity;
import com.example.geotracker.presentation.ViewModelKey;
import com.example.geotracker.presentation.ViewModelModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelModule.class})
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    @PerActivity
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);
}
