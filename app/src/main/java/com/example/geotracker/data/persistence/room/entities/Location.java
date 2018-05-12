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
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    @ColumnInfo(name = DbConstants.Location.COL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbConstants.Location.COL_LATITUDE)
    private double latitude;

    @ColumnInfo(name = DbConstants.Location.COL_LONGITUDE)
    private double longitude;

    @ColumnInfo(name = DbConstants.Location.COL_TIMESTAMP)
    private long timestamp;

    @ColumnInfo(name = DbConstants.Location.COL_JOURNEY_ID)
    private long journeyId;

    public Location(long id, double latitude, double longitude, long timestamp, long journeyId) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
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
        if (getTimestamp() != location.getTimestamp()) return false;
        return getJourneyId() == location.getJourneyId();
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
        result = 31 * result + (int) (getTimestamp() ^ (getTimestamp() >>> 32));
        result = 31 * result + (int) (getJourneyId() ^ (getJourneyId() >>> 32));
        return result;
    }
}
