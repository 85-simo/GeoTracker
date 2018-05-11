package com.example.geotracker.data.persistence.room.entities;

import android.content.Context;
import android.content.res.Resources;

import com.example.geotracker.rules.JodaTimeRule;

import net.danlew.android.joda.JodaTimeAndroid;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JourneyTest {
    @Rule
    public JodaTimeRule jodaTimeRule = new JodaTimeRule();

    @Test
    public void testEquality() {
        EqualsVerifier
                .forClass(Journey.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
