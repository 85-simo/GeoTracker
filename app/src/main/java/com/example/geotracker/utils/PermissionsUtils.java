package com.example.geotracker.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionsUtils {
    public static boolean arePermissionsNeeded(Context context, String... permissionsQuery) {
        boolean result = true;
        for (String aPermissionsQuery : permissionsQuery) {
            result = ActivityCompat.checkSelfPermission(context, aPermissionsQuery) != PackageManager.PERMISSION_GRANTED;
            if (!result) {
                break;
            }
        }
        return result;
    }
}
