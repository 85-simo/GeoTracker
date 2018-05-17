package com.example.geotracker.presentation.details.events;

import android.support.annotation.NonNull;

public class JourneyDetailsInfoEvent {
    @NonNull
    private final String journeyTitle;
    @NonNull
    private final String startedAt;
    @NonNull
    private final String completedAt;
    private final long durationMillis;
    private final double totalDistanceMeters;
    private final double averageSpeedKph;

    public JourneyDetailsInfoEvent(@NonNull String journeyTitle, @NonNull String startedAt, @NonNull String completedAt, long durationMillis, double totalDistanceMeters, double averageSpeedKph) {
        this.journeyTitle = journeyTitle;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.durationMillis = durationMillis;
        this.totalDistanceMeters = totalDistanceMeters;
        this.averageSpeedKph = averageSpeedKph;
    }

    @NonNull
    public String getJourneyTitle() {
        return journeyTitle;
    }

    @NonNull
    public String getStartedAt() {
        return startedAt;
    }

    @NonNull
    public String getCompletedAt() {
        return completedAt;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public double getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public double getAverageSpeedKph() {
        return averageSpeedKph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JourneyDetailsInfoEvent that = (JourneyDetailsInfoEvent) o;

        if (getDurationMillis() != that.getDurationMillis()) return false;
        if (Double.compare(that.getTotalDistanceMeters(), getTotalDistanceMeters()) != 0)
            return false;
        if (Double.compare(that.getAverageSpeedKph(), getAverageSpeedKph()) != 0) return false;
        if (!getJourneyTitle().equals(that.getJourneyTitle())) return false;
        if (!getStartedAt().equals(that.getStartedAt())) return false;
        return getCompletedAt().equals(that.getCompletedAt());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getJourneyTitle().hashCode();
        result = 31 * result + getStartedAt().hashCode();
        result = 31 * result + getCompletedAt().hashCode();
        result = 31 * result + (int) (getDurationMillis() ^ (getDurationMillis() >>> 32));
        temp = Double.doubleToLongBits(getTotalDistanceMeters());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getAverageSpeedKph());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
