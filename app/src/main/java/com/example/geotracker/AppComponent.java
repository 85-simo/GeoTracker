package com.example.geotracker;

import android.app.Application;

import com.example.geotracker.data.persistence.room.database.DatabaseModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, DatabaseModule.class})
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
