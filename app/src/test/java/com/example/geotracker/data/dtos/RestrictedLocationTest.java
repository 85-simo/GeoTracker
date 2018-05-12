package com.example.geotracker.data.dtos;

import com.example.geotracker.rules.JodaTimeRule;

import org.junit.Rule;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class RestrictedLocationTest {
    @Rule
    public JodaTimeRule jodaTimeRule = new JodaTimeRule();

    @Test
    public void testEquality() {
        EqualsVerifier.forClass(RestrictedLocation.class).usingGetClass().verify();
    }
}
