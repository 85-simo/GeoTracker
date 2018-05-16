package com.example.geotracker.utils;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateTimeUtils {
    public static final long INVALID_CONVERSION_MILLIS = Long.MIN_VALUE;

    public static long isoUTCDateTimeStringToMillis(@NonNull String iso8601UTCDateTimeString) {
        long result = INVALID_CONVERSION_MILLIS;
        try {
            result = ISODateTimeFormat.dateTime()
                    .parseDateTime(iso8601UTCDateTimeString)
                    .withZone(DateTimeZone.UTC)
                    .getMillis();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    @NonNull
    public static String utcMillisToDateTimeIsoString(long millis) {
        return ISODateTimeFormat
                .dateTime()
                .withZone(DateTimeZone.UTC)
                .print(millis);
    }

    @NonNull
    public static String isoUTCDateTimeStringToHumanReadable(@NonNull String isoUTCDateTimeString) {
        DateTimeFormatter humanReadableFormatter = DateTimeFormat.mediumDateTime();
        DateTime dateTime = ISODateTimeFormat.dateTime()
                .withZone(DateTimeZone.getDefault())
                .parseDateTime(isoUTCDateTimeString);
        return humanReadableFormatter.print(dateTime);
    }

    public static long millisBetween(@NonNull String isoUTCDateTimeStringStart, @NonNull String isoUTCDateTimeStringEnd) {
        DateTime dateTimeStart = new DateTime(isoUTCDateTimeStringStart);
        DateTime dateTimeEnd = new DateTime(isoUTCDateTimeStringEnd);
        Duration duration = new Duration(dateTimeStart, dateTimeEnd);
        return duration.getMillis();
    }

    public static String durationMillisToHumanReadable(long millisDuration, @NonNull String daysSuffix,
                                                       @NonNull String hoursSuffix, @NonNull String minutesSuffix, @NonNull String secondsSuffix) {
        Period period = new Period(millisDuration);
        PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
        if (period.getDays() > 0) {
            builder.appendDays()
                    .appendLiteral(" " + daysSuffix + " ");
        }
        if (period.getHours() > 0) {
            builder.appendHours()
                    .appendLiteral(" " + hoursSuffix + " ");
        }
        if (period.getMinutes() > 0) {
            builder.appendMinutes()
                    .appendLiteral(" " + minutesSuffix + " ");
        }
        if (period.getSeconds() > 0) {
            builder.appendSeconds()
                    .appendLiteral(" " + secondsSuffix + " ");
        }
        PeriodFormatter formatter = builder.toFormatter();
        return formatter.print(period);
    }

    @NonNull
    public static String timeBetween(@NonNull String isoUTCDateTimeStringStart, @NonNull String isoUTCDateTimeStringEnd,
                                     @NonNull String daysSuffix, @NonNull String hoursSuffix, @NonNull String minutesSuffix,
                                     @NonNull String secondsSuffix) {
        DateTime dateTimeStart = new DateTime(isoUTCDateTimeStringStart);
        DateTime dateTimeEnd = new DateTime(isoUTCDateTimeStringEnd);
        Period period = new Period(dateTimeStart, dateTimeEnd);
        PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
        if (period.getDays() > 0) {
            builder.appendDays()
                    .appendLiteral(" " + daysSuffix + " ");
        }
        if (period.getHours() > 0) {
            builder.appendHours()
                    .appendLiteral(" " + hoursSuffix + " ");
        }
        if (period.getMinutes() > 0) {
            builder.appendMinutes()
                    .appendLiteral(" " + minutesSuffix + " ");
        }
        if (period.getSeconds() > 0) {
            builder.appendSeconds()
                    .appendLiteral(" " + secondsSuffix + " ");
        }
        return builder.toString();
    }
}
