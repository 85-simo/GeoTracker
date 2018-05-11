package com.example.geotracker.data.persistence.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.geotracker.data.persistence.DbConstants;

@Entity(tableName = DbConstants.Location.TABLE_NAME,
        indices = { @Index(value = {DbConstants.Location.COL_JOURNEY_ID})},
        foreignKeys = @ForeignKey(
                entity = Journey.class,
                parentColumns = DbConstants.Journey.COL_ID,
                childColumns = DbConstants.Location.COL_JOURNEY_ID,
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE))
public class Location {
    @ColumnInfo(name = DbConstants.Location.COL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbConstants.Location.COL_LATITUDE)
    private double latitude;

    @ColumnInfo(name = DbConstants.Location.COL_LONGITUDE)
    private double longitude;

    @ColumnInfo(name = DbConstants.Location.COL_TIMESTAMP)
    private String timestamp;

    @ColumnInfo(name = DbConstants.Location.COL_JOURNEY_ID)
    private String journeyId;

    public Location(long id, double latitude, double longitude, String timestamp, String journeyId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.journeyId = journeyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (getId() != location.getId()) return false;
        if (Double.compare(location.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(location.getLongitude(), getLongitude()) != 0) return false;
        if (getTimestamp() != null ? !getTimestamp().equals(location.getTimestamp()) : location.getTimestamp() != null)
            return false;
        return getJourneyId() != null ? getJourneyId().equals(location.getJourneyId()) : location.getJourneyId() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getId() ^ (getId() >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        result = 31 * result + (getJourneyId() != null ? getJourneyId().hashCode() : 0);
        return result;
    }

}
