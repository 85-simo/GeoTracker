package com.example.geotracker.presentation.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.geotracker.R;
import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

@PerActivity
public class MapViewModel extends ViewModel {
    private MutableLiveData<PermissionsRequestEvent> permissionsRequestLiveEvent;
    private MutableLiveData<Void> permissionGrantLiveEvent;


    @Inject
    MapViewModel() {
        this.permissionsRequestLiveEvent = new SingleLiveEvent<>();
        this.permissionGrantLiveEvent = new SingleLiveEvent<>();
    }

    public LiveData<PermissionsRequestEvent> getObservablePermissionRequestStream() {
        return this.permissionsRequestLiveEvent;
    }

    public LiveData<Void> getObservablePermissionGrantStream() {
        return this.permissionGrantLiveEvent;
    }


    public void requestPermissions(String... requiredPermissions) {
        Schedulers.computation().scheduleDirect(() -> {
            PermissionsRequestEvent permissionsRequestEvent = new PermissionsRequestEvent(requiredPermissions, R.string.permission_access_location_denied_rationale);
            MapViewModel.this.permissionsRequestLiveEvent.postValue(permissionsRequestEvent);
        });
    }

    public void onPermissionsGranted() {
        Schedulers.computation().scheduleDirect(() -> MapViewModel.this.permissionGrantLiveEvent.postValue(null));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
