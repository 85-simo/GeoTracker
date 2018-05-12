package com.example.geotracker.data.dtos;

public class RestrictedLocation {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private final long identifier;
    private final double latitude;
    private final double longitude;
    private final String recordedAtIso;

    public RestrictedLocation(long identifier, double latitude, double longitude, String recordedAtIso) {
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAtIso = recordedAtIso;
    }

    public long getIdentifier() {
        return identifier;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRecordedAtIso() {
        return recordedAtIso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestrictedLocation that = (RestrictedLocation) o;

        if (getIdentifier() != that.getIdentifier()) return false;
        if (Double.compare(that.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(that.getLongitude(), getLongitude()) != 0) return false;
        return getRecordedAtIso() != null ? getRecordedAtIso().equals(that.getRecordedAtIso()) : that.getRecordedAtIso() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getIdentifier() ^ (getIdentifier() >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getRecordedAtIso() != null ? getRecordedAtIso().hashCode() : 0);
        return result;
    }
}
