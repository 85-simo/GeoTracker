package com.example.geotracker.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

/**
 * Simple class used for simple distance-related operations.
 */
public class DistanceUtils {
    public static final int METERS_IN_A_KILOMETER = 1000;

    public static double metersToKilometers(double meters) {
        return meters / METERS_IN_A_KILOMETER;
    }

    public static double kilometersToMeters(double kilometers) {
        return kilometers * METERS_IN_A_KILOMETER;
    }

    public static double computeDistance(List<LatLng> path) {
        return SphericalUtil.computeLength(path);
    }
}
