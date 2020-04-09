package com.example.geotracker.presentation.tracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.geotracker.R;
import com.example.geotracker.domain.base.GetInteractor;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.ActiveJourneys;
import com.example.geotracker.presentation.base.BaseService;
import com.example.geotracker.presentation.home.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Service implementation, compliant with Oreo restrictions which is in charge of receiving user's location updates and persisting them into the active journey's entity.
 * It does so by two interaction interfaces: whenever it receives a location update, it combines the usage of the two interactors its been given and combines them in order to
 * retrieve the active journey's instance, decode the previously stored path, simplify the current set of received locations, append the new locations to the previously recorded path, re-encode the result
 * and finally persist it.
 */
public class TrackingService extends BaseService {
    private static final int SIMPLIFICATION_TOLERANCE_METERS = 50;
    private static final String TRACKING_NOTIFICATION_CHANNEL_ID = TrackingService.class.getCanonicalName() + ".TRACKING_NOTIFICATION_CHANNEL_ID";
    private static final int TRACKING_NOTIFICATION_ID = 31141;

    @ActiveJourneys
    @Inject
    GetInteractor<Void, List<VisibleJourney>> getActiveJourneyInteractor;
    @Inject
    PersistInteractor<Void, VisibleJourney> persistJourneyInteractor;

    private boolean serviceRunning = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    private LocationManager locationManager;
    private LocationListener locationListener;


    public TrackingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.mLocationCallback = new LocationUpdateCallback();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LegacyLocationListener();
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

//            startLegacyLocationUpdates();
            startLocationUpdates();
        }
        else {
            Log.e("Service", "Start command ignored");
        }
        return START_STICKY;

    }

    private void startLocationUpdates() {
        Log.e("Service", "startLocationUpdates called");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            boolean fineLocGranted = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocGranted = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            Log.e("startLocationUpdates", "ACCESS_FINE_LOCATION: " + fineLocGranted);
            Log.e("startLocationUpdates", "ACCESS_COARSE_LOCATION: " + coarseLocGranted);
        }
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(TimeUnit.SECONDS.toMillis(5));
        Task<Void> task = this.mFusedLocationProviderClient.requestLocationUpdates(locationRequest, this.mLocationCallback, null);
        task.addOnFailureListener(e -> Log.e("LocationUpdatesRequest", "onFailure"));
    }

    @SuppressLint("MissingPermission")
    private void startLegacyLocationUpdates() {
        Log.e("Service", "startLegacyLocationUpdates called");
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, this.locationListener);
    }

    private void stopLocationUpdates() {
        this.mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);
    }

    private void stopLegacyLocationUpdates() {
        Log.e("Service", "stopLegacyLocationUpdates called");
        this.locationManager.removeUpdates(this.locationListener);
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
//        stopLegacyLocationUpdates();
        super.onDestroy();
    }

    /**
     * Class implementing the whole retrieval-decode-simplify-append-encode-store process.
     */
    private class LocationUpdateCallback extends LocationCallback {

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Log.e("LocationUpdateCallback", "Location availability: " + locationAvailability.isLocationAvailable());
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.e("LocationUpdateCallback", "onLocationResult called");
            Schedulers.computation().scheduleDirect(() -> {
                if (locationResult != null) {
                    List<Location> locations = locationResult.getLocations();
                    handleLocationUpdates(locations);
                }
            });
        }
    }

    private class LegacyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.e("LegacyLocationListener", "onLocationChanged");
            Schedulers.computation().scheduleDirect(() -> {
                List<Location> locations = Collections.singletonList(location);
                handleLocationUpdates(locations);
            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("LegacyLocationListener", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("LegacyLocationListener", "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("LegacyLocationListener", "onProviderDisabled: " + provider);
        }
    }

    private void handleLocationUpdates(List<Location> locations) {
        List<LatLng> locationsSequence = null;
        if (!locations.isEmpty()) {
            locationsSequence = new ArrayList<>(locations.size());
            for (Location location : locations) {
                locationsSequence.add(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
        if (locationsSequence != null) {
            List<LatLng> finalLocationsSequence = locationsSequence;
            TrackingService.this.getActiveJourneyInteractor
                    .get(null)
                    .flatMapCompletable(visibleJourneys -> {
                        if (!visibleJourneys.isEmpty()) {
                            VisibleJourney activeJourney = visibleJourneys.get(0);
                            String previousEncodedPath = activeJourney.getEncodedPath();
                            List<LatLng> simplifiedNewLocations = PolyUtil.simplify(finalLocationsSequence, SIMPLIFICATION_TOLERANCE_METERS);
                            List<LatLng> aggregatedLocationsSequence = null;
                            if (!TextUtils.isEmpty(previousEncodedPath)) {
                                List<LatLng> previousLocationsSequence = PolyUtil.decode(previousEncodedPath);
                                aggregatedLocationsSequence = new ArrayList<>(previousLocationsSequence.size() + simplifiedNewLocations.size());
                                aggregatedLocationsSequence.addAll(previousLocationsSequence);
                            }
                            if (aggregatedLocationsSequence == null) {
                                aggregatedLocationsSequence = new ArrayList<>();
                            }
                            aggregatedLocationsSequence.addAll(simplifiedNewLocations);
                            String newEncodedPath = PolyUtil.encode(aggregatedLocationsSequence);
                            VisibleJourney visibleJourney = new VisibleJourney(activeJourney.getIdentifier(),
                                    activeJourney.isComplete(),
                                    activeJourney.getStartedAtUTCDateTimeIso(),
                                    activeJourney.getCompletedAtUTCDateTimeIso(),
                                    activeJourney.getTitle(),
                                    newEncodedPath);
                            return TrackingService.this.persistJourneyInteractor
                                    .persist(null, visibleJourney);
                        }
                        else {
                            return Completable.complete();
                        }
                    }).subscribe();
        }
    }
}
