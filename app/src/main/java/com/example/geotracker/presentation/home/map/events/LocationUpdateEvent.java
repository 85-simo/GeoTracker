package com.example.geotracker.presentation.home.map.events;

public class LocationUpdateEvent {
    private double lat;
    private double lng;

    public LocationUpdateEvent(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationUpdateEvent that = (LocationUpdateEvent) o;

        if (Double.compare(that.getLat(), getLat()) != 0) return false;
        return Double.compare(that.getLng(), getLng()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLat());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLng());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
