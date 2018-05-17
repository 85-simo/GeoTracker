package com.example.geotracker;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Annotation used for Dagger2 configuration: needed in order to restrict the annotated component's scope.
 * Components annotated with the {@link PerActivity} annotation will be kept in memory as long as the {@link android.app.Activity}
 * injecting them.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
