package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Location;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

class EntityToRestrictedLocationsMapper implements Function<List<Location>, List<RestrictedLocation>> {

    @Inject
    EntityToRestrictedLocationsMapper() {

    }

    @Override
    public List<RestrictedLocation> apply(List<Location> locations) throws Exception {
        List<RestrictedLocation> result = new ArrayList<>(locations.size());
        for (Location location : locations) {
            String recordingDateTimeUTC = DateTimeUtils.utcMillisToDateTimeIsoString(location.getTimestamp());
            result.add(new RestrictedLocation(location.getId(),
                    location.getLatitude(),
                    location.getLongitude(),
                    recordingDateTimeUTC));
        }
        return result;
    }
}
