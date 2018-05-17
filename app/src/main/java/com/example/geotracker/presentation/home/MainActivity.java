package com.example.geotracker.presentation.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.home.journeys.fragments.JourneysFragment;
import com.example.geotracker.presentation.home.map.events.ActivityEvent;
import com.example.geotracker.presentation.home.map.events.NavigationEvent;
import com.example.geotracker.presentation.home.map.fragments.MapFragment;
import com.example.geotracker.presentation.map.events.MapEvent;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.presentation.tracking.TrackingService;
import com.example.geotracker.utils.PermissionsUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.main_activity_bnv)
    BottomNavigationView mainActivityBnv;
    @BindView(R.id.main_activity_content_fl)
    FrameLayout mainActivityContentFl;
    @BindView(R.id.activity_main_tracking_fab)
    FloatingActionButton activityMainTrackingFab;

    private MainViewModel viewModel;
    private MaterialDialog journeyCreationDialog;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ViewCompat.setElevation(this.mainActivityBnv, getResources().getDimensionPixelSize(R.dimen.bars_elevation));
        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MainViewModel.class);
        this.viewModel.getObservablePermissionRequestStream()
                .observe(this, new PermissionRequestObserver(this));
        this.viewModel.getObservableTrackingStateStream()
                .observe(this, new TrackingStateChangesObserver(this));
        this.viewModel.getObservableMapEventStream()
                .observe(this, new MapEventsStreamObserver(this));
        this.viewModel.getObservableActivityEventStream()
                .observe(this, new ActivityEventsStreamObserver(this));
        this.viewModel.getObservableNavigationEventsStream()
                .observe(this, new NavigationEventsStreamObserver(this));
        this.mainActivityBnv.setOnNavigationItemSelectedListener(item -> {
            boolean wasChecked = item.isChecked();
            MainActivity.this.viewModel.onTabSelected(item.getItemId(), wasChecked);
            return true;
        });
        selectTab(R.id.item_map);
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.mLocationCallback = new UserLocationUpdateCallback(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.journeyCreationDialog != null && !this.journeyCreationDialog.isShowing()) {
            this.journeyCreationDialog.show();
        }
    }

    @Override
    protected void onPause() {
        if (this.journeyCreationDialog != null && this.journeyCreationDialog.isShowing()) {
            this.journeyCreationDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    protected void onPermissionsGranted(String[] grantedPermissions) {
        this.viewModel.onPermissionsGranted();
    }

    @OnClick(R.id.activity_main_tracking_fab)
    void onTrackingButtonClick() {
        if (PermissionsUtils.arePermissionsNeeded(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.viewModel.requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        else {
            this.viewModel.onTrackingButtonClicked();
        }
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


    private static class PermissionRequestObserver implements Observer<PermissionsRequestEvent> {
        private WeakReference<MainActivity> activityWeakReference;

        PermissionRequestObserver(MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onChanged(@Nullable PermissionsRequestEvent permissionsRequestEvent) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (mainActivity != null) {
                if (permissionsRequestEvent != null) {
                    mainActivity.requestPermissions(permissionsRequestEvent.getRequestRationaleStringResId(), permissionsRequestEvent.getRequestedPermissions());
                }
            }
        }
    }

    private static class TrackingStateChangesObserver implements Observer<Boolean> {
        private WeakReference<MainActivity> activityWeakReference;

        TrackingStateChangesObserver(MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onChanged(@Nullable Boolean trackingState) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (mainActivity != null) {
                if (trackingState != null) {
                    Drawable stateDrawable = null;
                    if (trackingState) {
                        stateDrawable = AppCompatResources.getDrawable(mainActivity.getApplicationContext(), R.drawable.ic_stop);
                    } else {
                        stateDrawable = AppCompatResources.getDrawable(mainActivity.getApplicationContext(), R.drawable.ic_play_arrow);
                    }
                    mainActivity.activityMainTrackingFab.setImageDrawable(stateDrawable);
                }
            }
        }
    }

    private void selectTab(@IdRes int tabItemId) {
        Fragment fragment = null;
        String transactionTag = null;
        boolean trackingSwitchVisible = true;
        switch (tabItemId) {
            case R.id.item_map:
                fragment = MapFragment.newInstance();
                transactionTag = MapFragment.TAG;
                break;
            case R.id.item_list:
                fragment = JourneysFragment.newInstance();
                transactionTag = JourneysFragment.TAG;
                trackingSwitchVisible = false;
                break;
        }
        if (!TextUtils.isEmpty(transactionTag) && fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            MenuItem menuItem = this.mainActivityBnv.getMenu().findItem(tabItemId);
            if (!menuItem.isChecked()) {
                menuItem.setChecked(true);
            }
            int selectedItemOrder = menuItem.getOrder();
            if (selectedItemOrder == 0) {
                fragmentTransaction = fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
            else {
                fragmentTransaction = fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            }
            fragmentTransaction.replace(R.id.main_activity_content_fl, fragment, transactionTag)
                    .commit();
        }
        this.activityMainTrackingFab.setVisibility(trackingSwitchVisible ? View.VISIBLE : View.GONE);
    }


    private static class MapEventsStreamObserver implements Observer<MapEvent> {
        private WeakReference<MainActivity> activityWeakReference;

        MapEventsStreamObserver(MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onChanged(@Nullable MapEvent mapEvent) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (mainActivity != null) {
                if (mapEvent != null) {
                    Intent serviceIntent = new Intent(mainActivity, TrackingService.class);
                    switch (mapEvent.getType()) {
                        case TYPE_START_TRACKING_SERVICE:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                mainActivity.startForegroundService(serviceIntent);
                            } else {
                                mainActivity.startService(serviceIntent);
                            }
                            break;
                        case TYPE_STOP_TRACKING_SERVICE:
                            mainActivity.stopService(serviceIntent);
                            break;
                        case TYPE_SHOW_NEW_JOURNEY_CREATOR:
                            new MaterialDialog.Builder(mainActivity)
                                    .title(R.string.journey_creation_dialog_title)
                                    .input(null, null, false, (dialog, input) -> {
                                        mainActivity.viewModel.onJourneyCreationValuesSubmitted(input.toString());
                                        mainActivity.journeyCreationDialog = null;
                                    })
                                    .show();
                            break;
                    }
                }
            }
        }
    }

    private static class ActivityEventsStreamObserver implements Observer<ActivityEvent> {
        private WeakReference<MainActivity> activityWeakReference;

        ActivityEventsStreamObserver(@NonNull MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onChanged(@Nullable ActivityEvent activityEvent) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (activityEvent != null && mainActivity != null)  {
                switch (activityEvent.getType()) {
                    case TYPE_TAB_SELECTED:
                        ActivityEvent.TabType selectedTab = activityEvent.getTabType();
                        switch (selectedTab) {
                            case TAB_TYPE_MAP:
                                mainActivity.selectTab(R.id.item_map);
                                break;
                            case TAB_TYPE_JOURNEYS:
                                mainActivity.selectTab(R.id.item_list);
                                break;
                        }
                        break;
                }
            }
        }
    }

    private static class NavigationEventsStreamObserver implements Observer<NavigationEvent> {
        private WeakReference<MainActivity> activityWeakReference;

        NavigationEventsStreamObserver(MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onChanged(@Nullable NavigationEvent navigationEvent) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (mainActivity != null && navigationEvent != null) {
                switch (navigationEvent.getType()) {
                    case TYPE_ACTIVITY:
                        Intent intent = new Intent(mainActivity, navigationEvent.getActivityClass());
                        intent.putExtras(navigationEvent.getExtras());
                        mainActivity.startActivity(intent);
                        break;
                }
            }
        }
    }

    private static class UserLocationUpdateCallback extends LocationCallback {
        private WeakReference<MainActivity> activityWeakReference;

        UserLocationUpdateCallback(MainActivity mainActivity) {
            this.activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            MainActivity mainActivity = this.activityWeakReference.get();
            if (mainActivity != null && locationResult != null) {
                Location lastLocation = locationResult.getLastLocation();
                mainActivity.viewModel.signalLocationUpdateReceived(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        }
    }
}
