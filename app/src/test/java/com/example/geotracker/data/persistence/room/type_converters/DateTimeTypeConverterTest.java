package com.example.geotracker.data.persistence.room.type_converters;

import com.example.geotracker.data.persistence.room.database.type_converters.DateTimeTypeConverter;
import com.example.geotracker.rules.JodaTimeRule;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DateTimeTypeConverterTest {

    private DateTimeTypeConverter dateTimeTypeConverter;

    @Before
    public void setUp() {
        this.dateTimeTypeConverter = new DateTimeTypeConverter();
    }

    @Test
    public void test() {
        // TODO
    }
}
