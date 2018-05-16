package com.example.geotracker.presentation.tracking;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.geotracker.R;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleLocation;
import com.example.geotracker.presentation.base.BaseService;
import com.example.geotracker.presentation.home.MainActivity;
import com.example.geotracker.utils.DateTimeUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class TrackingService extends BaseService {
    private static final String TRACKING_NOTIFICATION_CHANNEL_ID = TrackingService.class.getCanonicalName() + ".TRACKING_NOTIFICATION_CHANNEL_ID";
    private static final int TRACKING_NOTIFICATION_ID = 31141;

    @Inject
    PersistInteractor<Void, List<VisibleLocation>> visibleLocationsPersistInteractor;

    private boolean serviceRunning = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;


    public TrackingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.mLocationCallback = new LocationUpdateCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!serviceRunning) {
            this.serviceRunning = true;
            Log.e("Service", "Service started");
            Intent activityIntent = new Intent(this, MainActivity.class);
            PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), TRACKING_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_tracking)
                    .setContentTitle(getString(R.string.tracking_service_notification_text))
                    .setContentIntent(activityPendingIntent);
            Notification notification = mBuilder.build();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel notificationChannel = new NotificationChannel(TRACKING_NOTIFICATION_CHANNEL_ID, getString(R.string.tracking_service_notification_channel_name), importance);
                    notificationChannel.setSound(null, null);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
            }
            startForeground(TRACKING_NOTIFICATION_ID, notification);

            startLocationUpdates();
        }
        else {
            Log.e("Service", "Start command ignored");
        }
        return START_STICKY;

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(TimeUnit.SECONDS.toMillis(5));
        this.mFusedLocationProviderClient.requestLocationUpdates(locationRequest, this.mLocationCallback, null);
    }

    private void stopLocationUpdates() {
        this.mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        this.serviceRunning = false;
        stopLocationUpdates();
        super.onDestroy();
    }

    private class LocationUpdateCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Schedulers.computation().scheduleDirect(() -> {
                if (locationResult != null) {
                    List<Location> locations = locationResult.getLocations();
                    List<VisibleLocation> visibleLocations = new ArrayList<>(locations.size());
                    for (Location location : locations) {
                        String recordedAtIso = DateTimeUtils.utcMillisToDateTimeIsoString(System.currentTimeMillis());
                        VisibleLocation visibleLocation = new VisibleLocation(VisibleLocation.GENERATE_NEW_IDENTIFIER,
                                location.getLatitude(),
                                location.getLongitude(),
                                recordedAtIso);
                        visibleLocations.add(visibleLocation);
                    }
                    TrackingService.this.visibleLocationsPersistInteractor
                            .persist(null, visibleLocations)
                            .subscribe();
                }
            });
        }
    }
}
