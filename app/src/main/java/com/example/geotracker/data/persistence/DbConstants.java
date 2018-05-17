package com.example.geotracker.data.persistence;

/**
 * Utility class representing a way for avoiding hardcoded Strings to be used for both queries and column names in db-def classes.
 */
public class DbConstants {
    public final class Journey {
        public static final String TABLE_NAME = "journeys";
        public static final String COL_ID = "id";
        public static final String COL_COMPLETE = "is_complete";
        public static final String COL_STARTED_AT = "started_at";
        public static final String COL_COMPLETED_AT = "completed_at";
        public static final String COL_TITLE = "title";
        public static final String COL_ENCODED_PATH = "encoded_path";
    }
}
