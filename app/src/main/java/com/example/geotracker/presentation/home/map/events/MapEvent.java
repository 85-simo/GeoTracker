package com.example.geotracker.presentation.map.events;

import android.support.annotation.NonNull;

public class MapEvent {
    public enum Type {
        TYPE_SHOW_NEW_JOURNEY_CREATOR,
        TYPE_START_TRACKING_SERVICE,
        TYPE_STOP_TRACKING_SERVICE
    }
    @NonNull
    private Type type;

    private long activeJourneyId;

    public MapEvent(@NonNull Type type, long activeJourneyId) {
        this.type = type;
        this.activeJourneyId = activeJourneyId;
    }

    @NonNull
    public Type getType() {
        return type;
    }


    public long getActiveJourneyId() {
        return activeJourneyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapEvent mapEvent = (MapEvent) o;

        if (getActiveJourneyId() != mapEvent.getActiveJourneyId()) return false;
        return getType() == mapEvent.getType();
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + (int) (getActiveJourneyId() ^ (getActiveJourneyId() >>> 32));
        return result;
    }
}
