package com.example.geotracker.presentation.details.events;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

public class JourneyDetailsPathEvent {
    @NonNull
    private PolylineOptions pathPolylineOptions;
    @NonNull
    private LatLngBounds pathBoundingBox;

    public JourneyDetailsPathEvent(@NonNull PolylineOptions pathPolylineOptions, @NonNull LatLngBounds pathBoundingBox) {
        this.pathPolylineOptions = pathPolylineOptions;
        this.pathBoundingBox = pathBoundingBox;
    }

    @NonNull
    public PolylineOptions getPathPolylineOptions() {
        return pathPolylineOptions;
    }

    @NonNull
    public LatLngBounds getPathBoundingBox() {
        return pathBoundingBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JourneyDetailsPathEvent that = (JourneyDetailsPathEvent) o;

        if (!getPathPolylineOptions().equals(that.getPathPolylineOptions())) return false;
        return getPathBoundingBox().equals(that.getPathBoundingBox());
    }

    @Override
    public int hashCode() {
        int result = getPathPolylineOptions().hashCode();
        result = 31 * result + getPathBoundingBox().hashCode();
        return result;
    }
}
