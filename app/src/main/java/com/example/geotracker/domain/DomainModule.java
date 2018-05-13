package com.example.geotracker.domain;

import com.example.geotracker.domain.interactors.InteractorsModule;
import com.example.geotracker.domain.mappers.DomainMappersModule;

import dagger.Module;

@Module(includes = {DomainMappersModule.class, InteractorsModule.class})
public class DomainModule {

}
