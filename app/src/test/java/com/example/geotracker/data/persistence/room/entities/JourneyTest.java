package com.example.geotracker.data.persistence.room.entities;

import com.example.geotracker.rules.JodaTimeRule;

import org.junit.Rule;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

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
