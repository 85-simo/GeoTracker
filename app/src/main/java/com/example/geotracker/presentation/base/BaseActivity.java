package com.example.geotracker.presentation.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.geotracker.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import dagger.android.AndroidInjection;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    protected void requestPermissions(@StringRes int permissionRequestRationale, @NonNull String... requestedPermissions) {
        Dexter.withActivity(this)
                .withPermissions(requestedPermissions)
                .withListener(new PermissionsListener(permissionRequestRationale))
                .check();
    }

    private class PermissionsListener implements MultiplePermissionsListener {
        @StringRes
        private int permissionsRequestRationaleResId;

        PermissionsListener(@StringRes int permissionsRequestRationaleResId) {
            this.permissionsRequestRationaleResId = permissionsRequestRationaleResId;
        }

        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (!report.areAllPermissionsGranted()) {
                showPermissionRationale(this.permissionsRequestRationaleResId);
            }
            if (!report.getGrantedPermissionResponses().isEmpty()) {
                String[] grantedPermissions = new String[report.getGrantedPermissionResponses().size()];
                for (int i = 0; i < report.getGrantedPermissionResponses().size(); i++) {
                    PermissionGrantedResponse permissionGrantedResponse = report.getGrantedPermissionResponses().get(i);
                    grantedPermissions[i] = permissionGrantedResponse.getPermissionName();
                }
                onPermissionsGranted(grantedPermissions);
            }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            token.continuePermissionRequest();
        }
    }

    protected abstract void onPermissionsGranted(String[] grantedPermissions);

    protected void showSnackbar(@StringRes int textStringRes, boolean showSettingsButton) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), textStringRes, Snackbar.LENGTH_LONG);
        if (showSettingsButton) {
            snackbar.setAction(R.string.common_settings, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAppSettings();
                }
            });
        }
        snackbar.show();
    }

    protected void showPermissionRationale(@StringRes int rationaleResId) {
        showSnackbar(rationaleResId, true);
    }

    protected void openAppSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }
}