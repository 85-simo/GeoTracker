package com.example.geotracker.presentation.map;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.content.res.AppCompatResources;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.map.events.MapEvent;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.presentation.map.fragments.MapFragment;
import com.example.geotracker.presentation.tracking.TrackingService;

import java.lang.ref.WeakReference;

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

    private MapViewModel viewModel;
    private MaterialDialog journeyCreationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MapViewModel.class);
        this.viewModel.getObservablePermissionRequestStream()
                .observe(this, new PermissionRequestObserver(this));
        this.viewModel.getObservableTrackingStateStream()
                .observe(this, new TrackingStateChangesObserver(this));
        this.viewModel.getObservableMapEventStream()
                .observe(this, new MapEventsStreamObserver(this));
        MapFragment mapFragment = MapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_content_fl, mapFragment, MapFragment.TAG)
                .commit();
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
    protected void onPermissionsGranted(String[] grantedPermissions) {
        this.viewModel.onPermissionsGranted();
    }

    @OnClick(R.id.activity_main_tracking_fab)
    void onTrackingButtonClick() {
        this.viewModel.onTrackingButtonClicked();
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
}
