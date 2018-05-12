package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Location;
import com.example.geotracker.utils.DateTimeUtils;

import io.reactivex.functions.Function;

public class EntityToRestrictedLocationMapper implements Function<Location, RestrictedLocation> {

    @Override
    public RestrictedLocation apply(Location location) throws Exception {
        String recordingDateTimeUTC = DateTimeUtils.utcMillisToDateTimeIsoString(location.getTimestamp());
        return new RestrictedLocation(location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                recordingDateTimeUTC);
    }
}
