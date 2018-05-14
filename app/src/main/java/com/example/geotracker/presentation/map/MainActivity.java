package com.example.geotracker.presentation.map;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.widget.FrameLayout;

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.presentation.map.fragments.MapFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseFragmentActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.main_activity_bnv)
    BottomNavigationView mainActivityBnv;
    @BindView(R.id.main_activity_content_fl)
    FrameLayout mainActivityContentFl;

    private MapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MapViewModel.class);
        this.viewModel.getObservablePermissionRequestStream()
                .observe(this, new PermissionRequestObserver());
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


    private class PermissionRequestObserver implements Observer<PermissionsRequestEvent> {


        @Override
        public void onChanged(@Nullable PermissionsRequestEvent permissionsRequestEvent) {
            if (permissionsRequestEvent != null) {
                MainActivity.super.requestPermissions(permissionsRequestEvent.getRequestRationaleStringResId(), permissionsRequestEvent.getRequestedPermissions());
            }
        }
    }
}
