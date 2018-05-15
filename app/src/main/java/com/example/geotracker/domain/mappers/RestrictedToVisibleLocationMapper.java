package com.example.geotracker.domain.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.domain.dtos.VisibleLocation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

class RestrictedToVisibleLocationsMapper implements Function<List<RestrictedLocation>, List<VisibleLocation>> {
    @Inject
    RestrictedToVisibleLocationsMapper() {

    }

    @Override
    public List<VisibleLocation> apply(List<RestrictedLocation> restrictedLocations) throws Exception {
        List<VisibleLocation> result = new ArrayList<>(restrictedLocations.size());
        for (RestrictedLocation restrictedLocation : restrictedLocations) {
            result.add(new VisibleLocation(restrictedLocation.getIdentifier(),
                    restrictedLocation.getLatitude(),
                    restrictedLocation.getLongitude(),
                    restrictedLocation.getRecordedAtIso()));
        }
        return result;
    }
}
