package com.example.geotracker.presentation.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.geotracker.R;
import com.example.geotracker.domain.base.PersistInteractor;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.presentation.PerActivity;
import com.example.geotracker.presentation.map.events.PermissionsRequestEvent;
import com.example.geotracker.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class MapViewModel extends ViewModel {
    private MutableLiveData<PermissionsRequestEvent> permissionsRequestLiveEvent;
    private MutableLiveData<Void> permissionGrantLiveEvent;
    private MutableLiveData<Boolean> trackingStateStreamEvent;
    private RetrieveInteractor<Void, Boolean> retrieveTrackingStateInteractor;
    private PersistInteractor<Void, Boolean> persistTrackingStateInteractor;
    private CompositeDisposable compositeDisposable;

    @Inject
    MapViewModel(@NonNull RetrieveInteractor<Void, Boolean> retrieveTrackingStateInteractor,
                 @NonNull PersistInteractor<Void, Boolean> persistTrackingStateInteractor) {
        this.retrieveTrackingStateInteractor = retrieveTrackingStateInteractor;
        this.persistTrackingStateInteractor = persistTrackingStateInteractor;

        this.compositeDisposable = new CompositeDisposable();
        this.permissionsRequestLiveEvent = new SingleLiveEvent<>();
        this.permissionGrantLiveEvent = new SingleLiveEvent<>();
        this.trackingStateStreamEvent = new MutableLiveData<>();

        this.compositeDisposable.add(this.retrieveTrackingStateInteractor.retrieve(null)
                .subscribe(new TrackingStateUpdatesConsumer())
        );
    }

    public LiveData<PermissionsRequestEvent> getObservablePermissionRequestStream() {
        return this.permissionsRequestLiveEvent;
    }

    public LiveData<Void> getObservablePermissionGrantStream() {
        return this.permissionGrantLiveEvent;
    }

    public LiveData<Boolean> getObservableTrackingStateStream() {
        return this.trackingStateStreamEvent;
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

    public void setTrackingState(boolean trackingActive) {
        Schedulers.computation().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                MapViewModel.this.compositeDisposable.add(MapViewModel.this.persistTrackingStateInteractor
                        .persist(null, trackingActive)
                        .subscribe()
                );
            }
        });
    }

    @Override
    protected void onCleared() {
        this.compositeDisposable.dispose();
        super.onCleared();
    }

    private class TrackingStateUpdatesConsumer implements Consumer<Boolean> {

        @Override
        public void accept(Boolean trackingState) throws Exception {
            if (trackingState != null) {
                MapViewModel.this.trackingStateStreamEvent.postValue(trackingState);
            }
        }
    }
}
