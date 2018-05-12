package com.example.geotracker.utils;

import junit.framework.Assert;

import org.junit.Test;

public class DateTimeUtilsTest {
    private static final long TEST_TIMESTAMP = 1526148000000L; // Corresponds to 2018-05-12T18:00:00.000Z
    private static final long TEST_INVALID_TIMESTAMP = -1L;
    private static final String TEST_ISO_DATETIME_UTC = "2018-05-12T18:00:00.000Z"; // Corresponds to 1526148000000L
    private static final String TEST_INVALID_DATETIME = "2018-05-02";

    @Test
    public void givenWellKnownTimestamp_returnCorrespondingUTCDateTimeIsoString() {
        String convertedTimestamp = DateTimeUtils.utcMillisToDateTimeIsoString(TEST_TIMESTAMP);
        Assert.assertEquals(TEST_ISO_DATETIME_UTC, convertedTimestamp);
    }

    @Test
    public void givenWellKnownUTCDateTimeIsoString_returnCorrespondingTimestamp() {
        long convertedDateTime = DateTimeUtils.isoUTCDateTimeStringToMillis(TEST_ISO_DATETIME_UTC);
        Assert.assertEquals(TEST_TIMESTAMP, convertedDateTime);
    }

    @Test
    public void givenInvalidDateTimeIsoString_returnErrorCode() {
        long convertedDateTime = DateTimeUtils.isoUTCDateTimeStringToMillis(TEST_INVALID_DATETIME);
        Assert.assertEquals(DateTimeUtils.INVALID_CONVERSION_MILLIS, convertedDateTime);
    }
}
