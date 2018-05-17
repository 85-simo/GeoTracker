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

/**
 * Base {@link AppCompatActivity} class whose purpose is defining common injection method calls for all activities which are not supposed to be dealing
 * with fragments. It additionally encapsulate base logic for dealing with permissions requests/responses.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    /**
     * Method for explicitly requesting the user specific permissions grant.It also allows to specify a text meant to explain users why the app is
     * requesting such permissions.
     * @param permissionRequestRationale a string res id representing an explanation of the app's needs.
     * @param requestedPermissions a varargs field containing all permissions that are to be requested through this operation.
     */
    protected void requestPermissions(@StringRes int permissionRequestRationale, @NonNull String... requestedPermissions) {
        Dexter.withActivity(this)
                .withPermissions(requestedPermissions)
                .withListener(new PermissionsListener(permissionRequestRationale))
                .check();
    }


    /**
     * Listener class that gets notified of whether the user has granted the previously requested permissions.
     */
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
            // Request permissions again!
            token.continuePermissionRequest();
        }
    }

    /**
     * Abstract method to be implemented by extending Activities which gets called whenever the user has granted at least one permission within the
     * set of requested permissions.
     * @param grantedPermissions a varargs field representing all permissions granted during the last invoked operation.
     */
    protected abstract void onPermissionsGranted(String[] grantedPermissions);

    /**
     * Utility method used for providing a quick handle to showing a Snackbar with a "Go to app settings" side button.
     * @param textStringRes A string res id representing the Snackbar's text
     * @param showSettingsButton whether or not the displayed Snackbar should provide the user with the possibility of quickly open app settings.
     */
    protected void showSnackbar(@StringRes int textStringRes, boolean showSettingsButton) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), textStringRes, Snackbar.LENGTH_LONG);
        if (showSettingsButton) {
            snackbar.setAction(R.string.common_settings, v -> openAppSettings());
        }
        snackbar.show();
    }

    /**
     * Utility method used for displaying a permissions' request rationale in a Snackbar with a link to app settings.
     * @param rationaleResId a string res id representing the rationale text to be displayed.
     */
    protected void showPermissionRationale(@StringRes int rationaleResId) {
        showSnackbar(rationaleResId, true);
    }

    /**
     * Utility method used for requesting the OS to redirect the user to the app's settings screen.
     */
    protected void openAppSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }
}