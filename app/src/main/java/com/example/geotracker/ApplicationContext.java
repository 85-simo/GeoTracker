package com.example.geotracker;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
/**
 * Qualifier annotation used for helping Dagger distinguish between different instances of object belonging to the same class:
 * the {@link ApplicationContext} annotation is used for denoting that the annotated {@link android.content.Context} instance is
 * referring to the Application's context.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
