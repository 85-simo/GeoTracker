package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedLocation;
import com.example.geotracker.data.persistence.room.entities.Location;
import com.example.geotracker.utils.DateTimeUtils;

import io.reactivex.functions.Function;

class RestrictedToEntityLocationMapper implements Function<RestrictedLocation, Location> {

    @Override
    public Location apply(RestrictedLocation restrictedLocation) {
        long entityTimestamp = DateTimeUtils.isoUTCDateTimeStringToMillis(restrictedLocation.getRecordedAtIso());
        return new Location(restrictedLocation.getIdentifier(),
                restrictedLocation.getLatitude(),
                restrictedLocation.getLongitude(),
                entityTimestamp,
                Long.MIN_VALUE);
    }
}
