package com.example.geotracker.presentation.journeys.events;

import android.support.annotation.NonNull;

import com.example.geotracker.domain.dtos.VisibleJourney;

import java.util.List;

public class JourneysEvent {
    public enum Type {
        TYPE_JOURNEYS_LIST_CHANGED
    }

    @NonNull
    private Type type;
    @NonNull
    private List<VisibleJourney> latestAvailableJourneys;

    public JourneysEvent(@NonNull Type type, @NonNull List<VisibleJourney> latestAvailableJourneys) {
        this.type = type;
        this.latestAvailableJourneys = latestAvailableJourneys;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @NonNull
    public List<VisibleJourney> getLatestAvailableJourneys() {
        return latestAvailableJourneys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JourneysEvent that = (JourneysEvent) o;

        if (getType() != that.getType()) return false;
        return getLatestAvailableJourneys().equals(that.getLatestAvailableJourneys());
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getLatestAvailableJourneys().hashCode();
        return result;
    }
}
