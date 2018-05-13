package com.example.geotracker;

import android.app.Application;

import com.example.geotracker.data.DataModule;
import com.example.geotracker.domain.DomainModule;
import com.example.geotracker.presentation.map.MapActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, DataModule.class, DomainModule.class, MapActivityModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        @BindsInstance
        Builder withDatabaseName(@DatabaseName String databaseName);

        AppComponent build();
    }

    void inject(GeoTrackerApplication application);
}
