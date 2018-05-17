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

/**
 * Class encapsulating all operations needed for datetime conversions and storage project-wise.
 */
public class DateTimeUtils {
    /**
     * Value used for indicating that a certain conversion whose return type was supposed to represent milliseconds had somehow failed.
     */
    public static final long INVALID_CONVERSION_MILLIS = Long.MIN_VALUE;

    /**
     * Static method used for converting an input datetime {@link String} to the corresponding timestamp, given the input {@link String} has been specified
     * using the correct format (yyyy-mm-dd'T'HH:mm:ss.SSSZ)
     * @param iso8601UTCDateTimeString the input datetime {@link String} we wish to convert (UTC timezone)
     * @return the timestamp represented by the input {@link String}
     */
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

    /**
     * Static method used for converting an input timestamp into its ISO8601 full String representation.
     * @param millis the input timestamp
     * @return a datetime {@link String} in the yyyy-mm-dd'T'HH:mm:ss.SSSZ format (UTC timezone)
     */
    @NonNull
    public static String utcMillisToDateTimeIsoString(long millis) {
        return ISODateTimeFormat
                .dateTime()
                .withZone(DateTimeZone.UTC)
                .print(millis);
    }

    /**
     * Utility method used for converting an input ISO8601 datetime representation into a human-readable format.
     * @param isoUTCDateTimeString the input ISO8601 (UTC timezone) {@link String}
     * @return a {@link String} representing the input value in a user-friendly fashion
     */
    @NonNull
    public static String isoUTCDateTimeStringToHumanReadable(@NonNull String isoUTCDateTimeString) {
        DateTimeFormatter humanReadableFormatter = DateTimeFormat.mediumDateTime();
        DateTime dateTime = ISODateTimeFormat.dateTime()
                .withZone(DateTimeZone.getDefault())
                .parseDateTime(isoUTCDateTimeString);
        return humanReadableFormatter.print(dateTime);
    }

    /**
     * Static method used for computing the amount of milliseconds between to ISO8601 input datetime {@link String} objects.
     * @param isoUTCDateTimeStringStart the starting datetime
     * @param isoUTCDateTimeStringEnd the ending datetime
     * @return millis between the input datetime Strings
     */
    public static long millisBetween(@NonNull String isoUTCDateTimeStringStart, @NonNull String isoUTCDateTimeStringEnd) {
        DateTime dateTimeStart = new DateTime(isoUTCDateTimeStringStart);
        DateTime dateTimeEnd = new DateTime(isoUTCDateTimeStringEnd);
        Duration duration = new Duration(dateTimeStart, dateTimeEnd);
        return duration.getMillis();
    }

    /**
     * Utility method used for converting a duration expressed in milliseconds into a user-friendly representation, additionally using input {@link String} values for representing
     * duration fields placeholders.
     * @param millisDuration the total duration in milliseconds
     * @param daysSuffix a {@link String} literal representing a placeholders for the 'days' field
     * @param hoursSuffix a {@link String} literal representing a placeholders for the 'hours' field
     * @param minutesSuffix a {@link String} literal representing a placeholders for the 'minutes' field
     * @param secondsSuffix a {@link String} literal representing a placeholders for the 'seconds' field
     * @return a user-friendly {@link String} representation of the input milliseconds as a duration.
     */
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
}
