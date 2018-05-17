package com.example.geotracker.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Class providing static utility methods related to permission handling. Useful for removing repeated code from classes
 * requiring explicit permissions check.
 */
public class PermissionsUtils {

    /**
     * Static method used for checking whether the user has granted certain specific permissions.
     * @param context the {@link Context} object needed for interacting with the OS
     * @param permissionsQuery a varargs string field type containing all required permissions
     * @return false if all specified permissions had already been granted, false otherwise.
     */
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
