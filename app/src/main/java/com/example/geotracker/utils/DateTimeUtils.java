package com.example.geotracker.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeUtils {

    public static DateTime UTCMillisToLocalDateTime(long utcMillis) {
        return new DateTime(utcMillis)
                .withZone(DateTimeZone.getDefault());
    }

    public static long localDateTimeToUTCMillis(DateTime dateTime) {
        return dateTime
                .withZone(DateTimeZone.UTC)
                .getMillis();
    }
}
