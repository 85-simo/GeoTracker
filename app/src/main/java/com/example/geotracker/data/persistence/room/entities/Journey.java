package com.example.geotracker.data.persistence.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.geotracker.data.persistence.DbConstants;

@Entity(tableName = DbConstants.Journey.TABLE_NAME)
public class Journey {
    public static final long GENERATE_NEW_IDENTIFIER = 0L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbConstants.Journey.COL_ID)
    private long id;

    @ColumnInfo(name = DbConstants.Journey.COL_COMPLETE)
    private boolean complete;

    public Journey(long id, boolean complete) {
        this.id = id;
        this.complete = complete;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journey journey = (Journey) o;

        if (getId() != journey.getId()) return false;
        return isComplete() == journey.isComplete();
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (isComplete() ? 1 : 0);
        return result;
    }

}
