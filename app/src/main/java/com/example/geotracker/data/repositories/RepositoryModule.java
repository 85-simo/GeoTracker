package com.example.geotracker.data.repositories;

import com.example.geotracker.data.repositories.Repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract Repository bindRepository(RepositoryImpl repository);
}
