package com.example.geotracker.data.dtos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RestrictedJourney {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private final long identifier;
    private final boolean complete;
    private final String startedAtUTCDateTimeIso;
    private final String completedAtUTCDateTimeIso;
    @NonNull
    private final String title;
    @Nullable
    private final String encodedPath;

    public RestrictedJourney(long identifier, boolean complete, String startedAtUTCDateTimeIso, String completedAtUTCDateTimeIso, @NonNull String title, String encodedPath) {
        this.identifier = identifier;
        this.complete = complete;
        this.startedAtUTCDateTimeIso = startedAtUTCDateTimeIso;
        this.completedAtUTCDateTimeIso = completedAtUTCDateTimeIso;
        this.title = title;
        this.encodedPath = encodedPath;
    }

    public long getIdentifier() {
        return identifier;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getStartedAtUTCDateTimeIso() {
        return startedAtUTCDateTimeIso;
    }

    public String getCompletedAtUTCDateTimeIso() {
        return completedAtUTCDateTimeIso;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getEncodedPath() {
        return encodedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestrictedJourney that = (RestrictedJourney) o;

        if (getIdentifier() != that.getIdentifier()) return false;
        if (isComplete() != that.isComplete()) return false;
        if (getStartedAtUTCDateTimeIso() != null ? !getStartedAtUTCDateTimeIso().equals(that.getStartedAtUTCDateTimeIso()) : that.getStartedAtUTCDateTimeIso() != null)
            return false;
        if (getCompletedAtUTCDateTimeIso() != null ? !getCompletedAtUTCDateTimeIso().equals(that.getCompletedAtUTCDateTimeIso()) : that.getCompletedAtUTCDateTimeIso() != null)
            return false;
        if (!getTitle().equals(that.getTitle())) return false;
        return getEncodedPath() != null ? getEncodedPath().equals(that.getEncodedPath()) : that.getEncodedPath() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getIdentifier() ^ (getIdentifier() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        result = 31 * result + (getStartedAtUTCDateTimeIso() != null ? getStartedAtUTCDateTimeIso().hashCode() : 0);
        result = 31 * result + (getCompletedAtUTCDateTimeIso() != null ? getCompletedAtUTCDateTimeIso().hashCode() : 0);
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + (getEncodedPath() != null ? getEncodedPath().hashCode() : 0);
        return result;
    }
}
