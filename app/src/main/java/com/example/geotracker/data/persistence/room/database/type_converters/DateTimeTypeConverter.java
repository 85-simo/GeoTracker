package com.example.geotracker.data.persistence.room.database.type_converters;

import android.arch.persistence.room.TypeConverter;

import com.example.geotracker.utils.DateTimeUtils;

import org.joda.time.DateTime;

public class DateTimeTypeConverter {
    @TypeConverter
    public static DateTime toLocalDateTime(long millisUTC) {
        return DateTimeUtils.UTCMillisToLocalDateTime(millisUTC);
    }

    @TypeConverter
    public static long toMillisUTC(DateTime localDateTime) {
        return DateTimeUtils.localDateTimeToUTCMillis(localDateTime);
    }
}
