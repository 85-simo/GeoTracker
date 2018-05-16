package com.example.geotracker.presentation.details;

import com.example.geotracker.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = { JourneyDetailsViewModelModule.class})
public interface JourneyDetailsActivitySubcomponent extends AndroidInjector<JourneyDetailsActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<JourneyDetailsActivity> {}
}
