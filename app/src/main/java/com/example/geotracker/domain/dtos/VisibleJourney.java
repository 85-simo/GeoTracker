package com.example.geotracker.domain.dtos;

public class VisibleJourney {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private final long identifier;
    private final boolean complete;
    private final String startedAtUTCDateTimeIso;
    private final String completedAtUTCDateTimeIso;

    public VisibleJourney(long identifier, boolean complete, String startedAtUTCDateTimeIso, String completedAtUTCDateTimeIso) {
        this.identifier = identifier;
        this.complete = complete;
        this.startedAtUTCDateTimeIso = startedAtUTCDateTimeIso;
        this.completedAtUTCDateTimeIso = completedAtUTCDateTimeIso;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisibleJourney that = (VisibleJourney) o;

        if (getIdentifier() != that.getIdentifier()) return false;
        if (isComplete() != that.isComplete()) return false;
        if (getStartedAtUTCDateTimeIso() != null ? !getStartedAtUTCDateTimeIso().equals(that.getStartedAtUTCDateTimeIso()) : that.getStartedAtUTCDateTimeIso() != null)
            return false;
        return getCompletedAtUTCDateTimeIso() != null ? getCompletedAtUTCDateTimeIso().equals(that.getCompletedAtUTCDateTimeIso()) : that.getCompletedAtUTCDateTimeIso() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getIdentifier() ^ (getIdentifier() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        result = 31 * result + (getStartedAtUTCDateTimeIso() != null ? getStartedAtUTCDateTimeIso().hashCode() : 0);
        result = 31 * result + (getCompletedAtUTCDateTimeIso() != null ? getCompletedAtUTCDateTimeIso().hashCode() : 0);
        return result;
    }
}
