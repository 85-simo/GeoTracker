package com.example.geotracker.presentation.home.map.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geotracker.ApplicationContext;
import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragment;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.home.MainViewModel;
import com.example.geotracker.presentation.home.map.events.LocationUpdateEvent;
import com.example.geotracker.presentation.home.map.events.PathEvent;
import com.example.geotracker.utils.DrawableUtils;
import com.example.geotracker.utils.PermissionsUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment implementation representing the app's main map. It's responsible for showing the user's position on a {@link GoogleMap}, following user's movements,
 * handling current active journey path changes (by drawing a polyline on the displayed map mean to represent the path followed by the user itself. It additionally
 * add a Marker in order to highlight where the currently active recording has started at.
 * All events, such as active path updates, user location updates, permissions grant etc. are notified through specific LiveData streams and come by an instance of
 * a {@link MainViewModel} object.
 */
public class MapFragment extends BaseFragment {
    private static final int DEFAULT_ZOOM_LEVEL = 17;
    private static final int POLYLINE_WIDTH = 10;
    public static final String TAG = MapFragment.class.getCanonicalName() + ".TAG";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    @ApplicationContext
    Context applicationContext;
    @BindDimen(R.dimen.location_marker_size)
    int locationMarkerSize;
    @BindColor(R.color.colorPrimary)
    @ColorInt
    int colorPrimary;
    Unbinder unbinder;

    private MainViewModel mainViewModel;
    private Polyline pathPolyline;
    private Marker startLocationMarker;
    private Marker endLocationMarker;

    @Nullable
    private GoogleMap googleMap;
    @Nullable
    private SupportMapFragment mapFragment;
    private boolean firstMapZoomExecuted = false;
    private boolean mapPreparing = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainViewModel = ViewModelProviders.of((BaseFragmentActivity) context, this.viewModelFactory).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.applicationContext);
        this.mainViewModel.getObservablePermissionGrantStream().observe(this, new PermissionGrantEventObserver(this));
        this.mapFragment = prepareMapFragment();
        this.mainViewModel.getObservableLocationUpdatesStream()
                .observe(this, new LocationUpdatesEventsObserver(this));
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_map_content_fl, mapFragment)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!this.mapPreparing && PermissionsUtils.arePermissionsNeeded(this.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.mainViewModel.requestPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else if (this.googleMap != null){
            setUpMap(this.googleMap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mapFragment != null && this.googleMap == null && !this.mapPreparing) {
            this.mapFragment.getMapAsync(new MapReadyCallback(this));
            this.mapPreparing = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("MissingPermission")
    private void showUserLocation() {
        if (this.googleMap != null) {
            this.googleMap.setMyLocationEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    private void centerMapOnUserPosition() {
        this.mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (MapFragment.this.googleMap != null && location != null) {
                        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MapFragment.this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM_LEVEL));
                    }
                    else {
                        centerMapOnUserPosition();
                    }
                });
    }

    private SupportMapFragment prepareMapFragment() {
        GoogleMapOptions options = new GoogleMapOptions()
                .mapToolbarEnabled(false)
                .zoomControlsEnabled(false)
                .compassEnabled(false)
                .mapType(GoogleMap.MAP_TYPE_NORMAL);
        return SupportMapFragment.newInstance(options);
    }

    private void setUpMap(@NonNull GoogleMap googleMap) {
        if (!googleMap.isMyLocationEnabled()) {
            showUserLocation();
        }
        if (!this.firstMapZoomExecuted) {
            centerMapOnUserPosition();
            firstMapZoomExecuted = true;
        }
    }

    private static class PermissionGrantEventObserver implements Observer<Void> {
        private WeakReference<MapFragment> fragmentWeakReference;

        PermissionGrantEventObserver(MapFragment mapFragment) {
            this.fragmentWeakReference = new WeakReference<>(mapFragment);
        }

        @Override
        public void onChanged(@Nullable Void aVoid) {
            MapFragment mapFragment = this.fragmentWeakReference.get();
            if (mapFragment != null) {
                if (mapFragment.googleMap != null && !mapFragment.mapPreparing) {
                    mapFragment.setUpMap(mapFragment.googleMap);
                }
            }
        }
    }

    private static class MapReadyCallback implements OnMapReadyCallback {
        private WeakReference<MapFragment> fragmentWeakReference;

        MapReadyCallback(MapFragment mapFragment) {
            this.fragmentWeakReference = new WeakReference<>(mapFragment);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapFragment mapFragment = this.fragmentWeakReference.get();
            if (mapFragment != null) {
                mapFragment.googleMap = googleMap;
                mapFragment.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mapFragment.mapPreparing = false;
                if (!PermissionsUtils.arePermissionsNeeded(mapFragment.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mapFragment.setUpMap(googleMap);
                }
                if (mapFragment.getActivity() != null) {
                    mapFragment.mainViewModel.getObservableJourneyEventStream()
                            .observe(mapFragment.getActivity(), new PathEventsObserver(mapFragment));
                }
            }
        }
    }

    private static class PathEventsObserver implements Observer<PathEvent> {
        private WeakReference<MapFragment> fragmentWeakReference;

        PathEventsObserver(MapFragment mapFragment) {
            this.fragmentWeakReference = new WeakReference<>(mapFragment);
        }

        @Override
        public void onChanged(@Nullable PathEvent pathEvent) {
            if (pathEvent != null) {
                MapFragment mapFragment = this.fragmentWeakReference.get();
                if (mapFragment != null) {
                    if (mapFragment.pathPolyline != null) {
                        mapFragment.pathPolyline.remove();
                    }
                    PolylineOptions options = pathEvent.getPathPolylineOptions()
                            .width(POLYLINE_WIDTH)
                            .color(ContextCompat.getColor(mapFragment.applicationContext, R.color.colorAccent));
                    List<LatLng> positions = options.getPoints();
                    LatLng startingPoint = null;
                    if (!positions.isEmpty()) {
                        startingPoint = positions.get(0);
                    }
                    MarkerOptions startLocationMarkerOptions = null;
                    if (startingPoint != null) {
                        Drawable startingPointDrawable = DrawableUtils.getTintedDrawable(mapFragment.applicationContext, R.drawable.ic_start, mapFragment.colorPrimary);
                        if (startingPointDrawable != null) {
                            Bitmap startingPointBitmap = DrawableUtils.setDrawableHeightWithKeepRatio(startingPointDrawable, mapFragment.locationMarkerSize);
                            startLocationMarkerOptions = new MarkerOptions()
                                    .position(startingPoint)
                                    .anchor(0.5f, 1.0f)
                                    .icon(BitmapDescriptorFactory.fromBitmap(startingPointBitmap));
                            if (mapFragment.startLocationMarker != null) {
                                mapFragment.startLocationMarker.remove();
                            }
                        }
                    }
                    if (mapFragment.googleMap != null) {
                        if (startLocationMarkerOptions != null) {
                            mapFragment.startLocationMarker = mapFragment.googleMap.addMarker(startLocationMarkerOptions);
                        }
                        mapFragment.pathPolyline = mapFragment.googleMap.addPolyline(options);
                    }
                }
            }
        }
    }

    private static class LocationUpdatesEventsObserver implements Observer<LocationUpdateEvent> {
        private WeakReference<MapFragment> fragmentWeakReference;

        LocationUpdatesEventsObserver(MapFragment mapFragment) {
            this.fragmentWeakReference = new WeakReference<>(mapFragment);
        }

        @Override
        public void onChanged(@Nullable LocationUpdateEvent locationUpdateEvent) {
            MapFragment mapFragment = this.fragmentWeakReference.get();
            if (mapFragment != null && locationUpdateEvent != null && mapFragment.googleMap != null) {
                LatLng userLatLng = new LatLng(locationUpdateEvent.getLat(), locationUpdateEvent.getLng());
                mapFragment.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM_LEVEL));
            }
        }
    }
}
