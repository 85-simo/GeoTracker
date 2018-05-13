package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.domain.dtos.VisibleLocation;

import io.reactivex.functions.Function;

public class RestrictedToVisibleLocationMapper implements Function<RestrictedLocation, VisibleLocation> {
    @Override
    public VisibleLocation apply(RestrictedLocation restrictedLocation) throws Exception {
        return new VisibleLocation(restrictedLocation.getIdentifier(),
                restrictedLocation.getLatitude(),
                restrictedLocation.getLongitude(),
                restrictedLocation.getRecordedAtIso());
    }
}
