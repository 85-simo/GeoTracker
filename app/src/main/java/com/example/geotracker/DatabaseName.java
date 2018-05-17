package com.example.geotracker;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Qualifier annotation used for helping Dagger distinguish between different instances of object belonging to the same class:
 * the {@link DatabaseName} annotation is used for annotating strings which have to be considered as indicating the database's filename.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseName {
}
