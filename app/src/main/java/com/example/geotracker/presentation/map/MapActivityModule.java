package com.example.geotracker.presentation.map;

import android.app.Activity;

import com.example.geotracker.presentation.ActivityModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ActivityModule.class}, subcomponents = {MapActivitySubcomponent.class})
public abstract class MapActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MapActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMapActivityInjectorFactory(MapActivitySubcomponent.Builder builder);
}
