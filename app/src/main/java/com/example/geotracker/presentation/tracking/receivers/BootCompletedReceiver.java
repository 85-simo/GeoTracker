package com.example.geotracker.presentation.tracking.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.geotracker.presentation.tracking.TrackingService;

/**
 * {@link BroadcastReceiver} implementation used for starting the {@link TrackingService} whenever the user reboots its device.
 * This component is supposed to be active if and only if there is one journey in the active state.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())) {
            Intent trackingServiceIntent = new Intent(context, TrackingService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(trackingServiceIntent);
            } else {
                context.startService(trackingServiceIntent);
            }
        }
    }
}
