package com.example.geotracker.presentation.map.events;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.PolylineOptions;

public class PathEvent {
    public enum Type {
        TYPE_PATH_UPDATE_RECEIVED
    }

    @NonNull
    private Type type;
    @NonNull
    private PolylineOptions pathPolylineOptions;

    public PathEvent(Type type, PolylineOptions pathPolylineOptions) {
        this.type = type;
        this.pathPolylineOptions = pathPolylineOptions;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @NonNull
    public PolylineOptions getPathPolylineOptions() {
        return pathPolylineOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathEvent that = (PathEvent) o;

        if (getType() != that.getType()) return false;
        return getPathPolylineOptions().equals(that.getPathPolylineOptions());
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getPathPolylineOptions().hashCode();
        return result;
    }
}
