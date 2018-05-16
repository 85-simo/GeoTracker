package com.example.geotracker.presentation.map.events;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public class PermissionsRequestEvent {
    @NonNull
    private final String[] requestedPermissions;
    @StringRes
    private final int requestRationaleStringResId;

    public PermissionsRequestEvent(@NonNull String[] requestedPermissions, int requestRationaleStringResId) {
        this.requestedPermissions = requestedPermissions;
        this.requestRationaleStringResId = requestRationaleStringResId;
    }

    @NonNull
    public String[] getRequestedPermissions() {
        return requestedPermissions;
    }

    public int getRequestRationaleStringResId() {
        return requestRationaleStringResId;
    }
}
