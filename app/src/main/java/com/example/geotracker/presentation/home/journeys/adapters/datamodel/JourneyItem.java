package com.example.geotracker.presentation.journeys.adapters.datamodel;

import android.support.annotation.NonNull;

public class JourneyItem {
    private long journeyId;
    @NonNull
    private String journeyTitle;
    @NonNull
    private String journeyCreationDateTimeUTC;
    private boolean journeyActive;

    public JourneyItem(long journeyId, @NonNull String journeyTitle, @NonNull String journeyCreationDateTimeUTC, boolean journeyActive) {
        this.journeyId = journeyId;
        this.journeyTitle = journeyTitle;
        this.journeyCreationDateTimeUTC = journeyCreationDateTimeUTC;
        this.journeyActive = journeyActive;
    }

    public long getJourneyId() {
        return journeyId;
    }

    @NonNull
    public String getJourneyTitle() {
        return journeyTitle;
    }

    @NonNull
    public String getJourneyCreationDateTimeUTC() {
        return journeyCreationDateTimeUTC;
    }

    public boolean isJourneyActive() {
        return journeyActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JourneyItem that = (JourneyItem) o;

        if (getJourneyId() != that.getJourneyId()) return false;
        if (isJourneyActive() != that.isJourneyActive()) return false;
        if (!getJourneyTitle().equals(that.getJourneyTitle())) return false;
        return getJourneyCreationDateTimeUTC().equals(that.getJourneyCreationDateTimeUTC());
    }

    @Override
    public int hashCode() {
        int result = (int) (getJourneyId() ^ (getJourneyId() >>> 32));
        result = 31 * result + getJourneyTitle().hashCode();
        result = 31 * result + getJourneyCreationDateTimeUTC().hashCode();
        result = 31 * result + (isJourneyActive() ? 1 : 0);
        return result;
    }
}
