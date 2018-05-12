package com.example.geotracker.data.dtos;

public class RestrictedLocation {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    private long identifier;
    private double latitude;
    private double longitude;
    private String recordedAtIso;

    public RestrictedLocation(long identifier, double latitude, double longitude, String recordedAtIso) {
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAtIso = recordedAtIso;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRecordedAtIso() {
        return recordedAtIso;
    }

    public void setRecordedAtIso(String recordedAtIso) {
        this.recordedAtIso = recordedAtIso;
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
