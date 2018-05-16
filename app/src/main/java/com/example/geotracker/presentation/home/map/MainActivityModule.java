package com.example.geotracker.presentation.home.map;

import android.app.Activity;

import com.example.geotracker.presentation.ActivityModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ActivityModule.class}, subcomponents = {MainActivitySubcomponent.class})
public abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMapActivityInjectorFactory(MainActivitySubcomponent.Builder builder);
}
