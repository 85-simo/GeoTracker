package com.example.geotracker.domain.dtos;

/**
 * Presentation-level entity representing a {@link com.example.geotracker.data.persistence.room.entities.Journey}, used for abstracting presentation-level
 * classes from unneeded details which are restricted to domain-level classes. See {@link com.example.geotracker.data.dtos.RestrictedJourney}
 */
public final class VisibleJourney {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private final long identifier;
    private final boolean complete;
    private final String startedAtUTCDateTimeIso;
    private final String completedAtUTCDateTimeIso;
    private final String title;
    private final String encodedPath;

    public VisibleJourney(long identifier, boolean complete, String startedAtUTCDateTimeIso, String completedAtUTCDateTimeIso, String title, String encodedPath) {
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

    public String getTitle() {
        return title;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisibleJourney that = (VisibleJourney) o;

        if (getIdentifier() != that.getIdentifier()) return false;
        if (isComplete() != that.isComplete()) return false;
        if (getStartedAtUTCDateTimeIso() != null ? !getStartedAtUTCDateTimeIso().equals(that.getStartedAtUTCDateTimeIso()) : that.getStartedAtUTCDateTimeIso() != null)
            return false;
        if (getCompletedAtUTCDateTimeIso() != null ? !getCompletedAtUTCDateTimeIso().equals(that.getCompletedAtUTCDateTimeIso()) : that.getCompletedAtUTCDateTimeIso() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
            return false;
        return getEncodedPath() != null ? getEncodedPath().equals(that.getEncodedPath()) : that.getEncodedPath() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getIdentifier() ^ (getIdentifier() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        result = 31 * result + (getStartedAtUTCDateTimeIso() != null ? getStartedAtUTCDateTimeIso().hashCode() : 0);
        result = 31 * result + (getCompletedAtUTCDateTimeIso() != null ? getCompletedAtUTCDateTimeIso().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getEncodedPath() != null ? getEncodedPath().hashCode() : 0);
        return result;
    }
}
