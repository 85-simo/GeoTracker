package com.example.geotracker.data.dtos;

public class RestrictedJourney {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private long identifier;
    private boolean complete;

    public RestrictedJourney(long identifier, boolean complete) {
        this.identifier = identifier;
        this.complete = complete;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestrictedJourney that = (RestrictedJourney) o;

        if (getIdentifier() != that.getIdentifier()) return false;
        return isComplete() == that.isComplete();
    }

    @Override
    public int hashCode() {
        int result = (int) (getIdentifier() ^ (getIdentifier() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        return result;
    }
}
