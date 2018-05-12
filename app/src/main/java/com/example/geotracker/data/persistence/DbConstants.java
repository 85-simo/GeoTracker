package com.example.geotracker.data.persistence;

public class DbConstants {
    public final class Journey {
        public static final String TABLE_NAME = "journeys";
        public static final String COL_ID = "id";
        public static final String COL_COMPLETE = "is_complete";
        public static final String COL_STARTED_AT = "started_at";
        public static final String COL_COMPLETED_AT = "completed_at";
    }

    public final class Location {
        public static final String TABLE_NAME = "locations";
        public static final String COL_ID = "id";
        public static final String COL_LATITUDE = "lat";
        public static final String COL_LONGITUDE = "lng";
        public static final String COL_TIMESTAMP = "timestamp";
        public static final String COL_JOURNEY_ID = "journey_id";
    }
}
