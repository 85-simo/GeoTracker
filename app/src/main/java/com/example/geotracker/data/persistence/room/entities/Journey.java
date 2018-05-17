package com.example.geotracker.data.persistence.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.geotracker.data.persistence.DbConstants;

@Entity(tableName = DbConstants.Journey.TABLE_NAME)
public class Journey {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbConstants.Journey.COL_ID)
    private long id;

    @ColumnInfo(name = DbConstants.Journey.COL_COMPLETE)
    private boolean complete;

    @ColumnInfo(name = DbConstants.Journey.COL_TITLE)
    private String title;

    @ColumnInfo(name = DbConstants.Journey.COL_STARTED_AT)
    private long startedAtTimestamp;

    @ColumnInfo(name = DbConstants.Journey.COL_COMPLETED_AT)
    private long completedAtTimestamp;

    @ColumnInfo(name = DbConstants.Journey.COL_ENCODED_PATH)
    private String encodedPath;

    public Journey(long id, boolean complete, String title, long startedAtTimestamp, long completedAtTimestamp, String encodedPath) {
        this.id = id;
        this.complete = complete;
        this.title = title;
        this.startedAtTimestamp = startedAtTimestamp;
        this.completedAtTimestamp = completedAtTimestamp;
        this.encodedPath = encodedPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartedAtTimestamp() {
        return startedAtTimestamp;
    }

    public void setStartedAtTimestamp(long startedAtTimestamp) {
        this.startedAtTimestamp = startedAtTimestamp;
    }

    public long getCompletedAtTimestamp() {
        return completedAtTimestamp;
    }

    public void setCompletedAtTimestamp(long completedAtTimestamp) {
        this.completedAtTimestamp = completedAtTimestamp;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journey journey = (Journey) o;

        if (getId() != journey.getId()) return false;
        if (isComplete() != journey.isComplete()) return false;
        if (getStartedAtTimestamp() != journey.getStartedAtTimestamp()) return false;
        if (getCompletedAtTimestamp() != journey.getCompletedAtTimestamp()) return false;
        if (getTitle() != null ? !getTitle().equals(journey.getTitle()) : journey.getTitle() != null)
            return false;
        return getEncodedPath() != null ? getEncodedPath().equals(journey.getEncodedPath()) : journey.getEncodedPath() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (int) (getStartedAtTimestamp() ^ (getStartedAtTimestamp() >>> 32));
        result = 31 * result + (int) (getCompletedAtTimestamp() ^ (getCompletedAtTimestamp() >>> 32));
        result = 31 * result + (getEncodedPath() != null ? getEncodedPath().hashCode() : 0);
        return result;
    }
}
