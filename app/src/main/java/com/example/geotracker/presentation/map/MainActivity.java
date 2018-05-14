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

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.presentation.map.fragments.MapFragment;
import com.example.geotracker.presentation.tracking.TrackingService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MapViewModel.class);
        this.viewModel.getObservablePermissionRequestStream()
                .observe(this, new PermissionRequestObserver());
        this.viewModel.getObservableTrackingStateStream()
                .observe(this, new TrackingStateChangesObserver());
        MapFragment mapFragment = MapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_content_fl, mapFragment, MapFragment.TAG)
                .commit();
    }

    @Override
    protected void onPermissionsGranted(String[] grantedPermissions) {
        this.viewModel.onPermissionsGranted();
    }

    @OnClick(R.id.activity_main_tracking_fab)
    void onTrackingButtonClick() {
        this.viewModel.onTrackingButtonClicked();
    }


    private class PermissionRequestObserver implements Observer<PermissionsRequestEvent> {
        @Override
        public void onChanged(@Nullable PermissionsRequestEvent permissionsRequestEvent) {
            if (permissionsRequestEvent != null) {
                MainActivity.super.requestPermissions(permissionsRequestEvent.getRequestRationaleStringResId(), permissionsRequestEvent.getRequestedPermissions());
            }
        }
    }

    private class TrackingStateChangesObserver implements Observer<Boolean> {

        @Override
        public void onChanged(@Nullable Boolean trackingState) {
            if (trackingState != null) {
                Drawable stateDrawable = null;
                Intent serviceIntent = new Intent(MainActivity.this, TrackingService.class);
                if (trackingState) {
                    stateDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_stop);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    }
                    else {
                        startService(serviceIntent);
                    }
                }
                else {
                    stateDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_play_arrow);
                    stopService(serviceIntent);
                }
                MainActivity.this.activityMainTrackingFab.setImageDrawable(stateDrawable);
            }
        }
    }
}
