package com.example.geotracker.presentation.details;

import android.app.Activity;

import com.example.geotracker.presentation.ActivityModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ActivityModule.class}, subcomponents = {JourneyDetailsActivitySubcomponent.class})
public abstract class JourneyDetailsActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(JourneyDetailsActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindJourneyDetailsActivityInjectorFactory(JourneyDetailsActivitySubcomponent.Builder builder);
}
