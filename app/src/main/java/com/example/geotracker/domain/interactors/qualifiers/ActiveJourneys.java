package com.example.geotracker.domain.interactors.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Qualifier used for annotating components of a certain type which need to deal with Journeys in the active state only.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActiveJourneys {
}
