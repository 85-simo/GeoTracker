package com.example.geotracker.presentation.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseActivity;
import com.example.geotracker.presentation.details.events.JourneyDetailsInfoEvent;
import com.example.geotracker.presentation.details.events.JourneyDetailsPathEvent;
import com.example.geotracker.utils.DateTimeUtils;
import com.example.geotracker.utils.DistanceUtils;
import com.example.geotracker.utils.DrawableUtils;
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

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class JourneyDetailsActivity extends BaseActivity {
    public static final String EXTRA_JOURNEY_ID = JourneyDetailsActivity.class.getCanonicalName() + ".EXTRA_JOURNEY_ID";
    private static final int POLYLINE_WIDTH = 10;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    @BindView(R.id.activity_journey_details_toolbar)
    Toolbar activityJourneyDetailsToolbar;
    @BindView(R.id.view_journey_info_total_duration_tv)
    TextView viewJourneyInfoTotalDurationTv;
    @BindView(R.id.view_journey_info_started_at_tv)
    TextView viewJourneyInfoStartedAtTv;
    @BindView(R.id.view_journey_info_ended_at_tv)
    TextView viewJourneyInfoEndedAtTv;
    @BindView(R.id.view_journey_info_distance_tv)
    TextView viewJourneyInfoDistanceTv;
    @BindView(R.id.view_journey_info_cl)
    ConstraintLayout viewJourneyInfoCl;
    @BindDimen(R.dimen.location_marker_size)
    int locationMarkerSize;
    @BindColor(R.color.colorPrimary)
    @ColorInt
    int colorPrimary;

    private JourneyDetailsViewModel viewModel;
    @Nullable
    private SupportMapFragment mapFragment;
    @Nullable
    private GoogleMap googleMap;

    private boolean mapPreparing = false;
    private Polyline mapPolyline;
    private Marker startLocationMarker;
    private Marker endLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);
        ButterKnife.bind(this);
        setSupportActionBar(this.activityJourneyDetailsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.journey_details_title);
        }
        BottomSheetBehavior.from(viewJourneyInfoCl);
        if (savedInstanceState == null) {
            GoogleMapOptions options = new GoogleMapOptions()
                    .mapToolbarEnabled(false)
                    .zoomControlsEnabled(false)
                    .compassEnabled(false)
                    .mapType(GoogleMap.MAP_TYPE_NORMAL);
            this.mapFragment = SupportMapFragment.newInstance(options);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_journey_details_map, this.mapFragment)
                    .commit();
        }
        this.viewModel = ViewModelProviders.of(this, this.viewModelProviderFactory).get(JourneyDetailsViewModel.class);
        if (getIntent() != null) {
            Intent startingIntent = getIntent();
            if (startingIntent.hasExtra(EXTRA_JOURNEY_ID)) {
                long selectedJourneyId = startingIntent.getLongExtra(EXTRA_JOURNEY_ID, Long.MIN_VALUE);
                this.viewModel.signalSelectedJourneyId(selectedJourneyId);
                this.viewModel.getJourneyInfoObservableStream()
                        .observe(this, new JourneyInfoObserver(this));

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mapFragment != null && this.googleMap == null && !this.mapPreparing) {
            this.mapFragment.getMapAsync(new MapReadyCallback(this));
            this.mapPreparing = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPermissionsGranted(String[] grantedPermissions) {
        // NOT NEEDED
    }

    private static class MapReadyCallback implements OnMapReadyCallback {
        private WeakReference<JourneyDetailsActivity> activityWeakReference;

        MapReadyCallback(JourneyDetailsActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            JourneyDetailsActivity activity = this.activityWeakReference.get();
            if (activity != null) {
                activity.googleMap = googleMap;
                activity.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                activity.mapPreparing = false;
                activity.viewModel.getJourneyPathObervableStream()
                        .observe(activity, new JourneyPathObserver(activity));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class JourneyInfoObserver implements Observer<JourneyDetailsInfoEvent> {
        private WeakReference<JourneyDetailsActivity> activityWeakReference;

        JourneyInfoObserver(JourneyDetailsActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onChanged(@Nullable JourneyDetailsInfoEvent journeyDetailsInfoEvent) {
            if (journeyDetailsInfoEvent != null) {
                JourneyDetailsActivity activity = this.activityWeakReference.get();
                if (activity != null) {
                    long journeyDurationMillis = journeyDetailsInfoEvent.getDurationMillis();
                    String journeyDurationString = DateTimeUtils.durationMillisToHumanReadable(journeyDurationMillis,
                            activity.getString(R.string.duration_days_suffix),
                            activity.getString(R.string.duration_hours_suffix),
                            activity.getString(R.string.duration_minutes_suffix),
                            activity.getString(R.string.duration_seconds_suffix));
                    String visibleDurationString = activity.getString(R.string.journey_details_info_total_duration, journeyDurationString);
                    double pathLengthMeters = journeyDetailsInfoEvent.getTotalDistanceMeters();
                    String pathLengthString = null;
                    if (pathLengthMeters > DistanceUtils.METERS_IN_A_KILOMETER) {
                        pathLengthString = activity.getString(R.string.journey_details_info_distance, DistanceUtils.metersToKilometers(pathLengthMeters), activity.getString(R.string.suffix_km));
                    }
                    else {
                        pathLengthString = activity.getString(R.string.journey_details_info_distance, pathLengthMeters, activity.getString(R.string.suffix_m));
                    }
                    String startedAtString = activity.getString(R.string.journey_details_info_start_datetime, journeyDetailsInfoEvent.getStartedAt());
                    String completedAtString = activity.getString(R.string.journey_details_info_end_datetime, journeyDetailsInfoEvent.getCompletedAt());
                    activity.viewJourneyInfoTotalDurationTv.setText(visibleDurationString);
                    activity.viewJourneyInfoStartedAtTv.setText(startedAtString);
                    activity.viewJourneyInfoEndedAtTv.setText(completedAtString);
                    activity.viewJourneyInfoDistanceTv.setText(pathLengthString);
                }
            }
        }
    }

    private static class JourneyPathObserver implements Observer<JourneyDetailsPathEvent> {
        private WeakReference<JourneyDetailsActivity> activityWeakReference;

        JourneyPathObserver(JourneyDetailsActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onChanged(@Nullable JourneyDetailsPathEvent journeyDetailsPathEvent) {
            JourneyDetailsActivity activity = this.activityWeakReference.get();
            if (activity != null) {
                if (journeyDetailsPathEvent != null) {
                    if (activity.mapPolyline != null) {
                        activity.mapPolyline.remove();
                    }
                    if (activity.startLocationMarker != null) {
                        activity.startLocationMarker.remove();
                    }
                    if (activity.endLocationMarker != null) {
                        activity.endLocationMarker.remove();
                    }
                    if (activity.googleMap != null) {
                        List<LatLng> locationSequence = journeyDetailsPathEvent.getPathPolylineOptions().getPoints();
                        if (!locationSequence.isEmpty()) {
                            activity.mapPolyline = activity.googleMap.addPolyline(journeyDetailsPathEvent.getPathPolylineOptions()
                                    .width(POLYLINE_WIDTH)
                                    .color(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAccent)));
                            LatLng startLocation = locationSequence.get(0);
                            LatLng endLocation = locationSequence.get(locationSequence.size() - 1);
                            Drawable startLocationDrawable = DrawableUtils.getTintedDrawable(activity.getApplicationContext(), R.drawable.ic_start, activity.colorPrimary);
                            Drawable endLocationDrawable = DrawableUtils.getTintedDrawable(activity.getApplicationContext(), R.drawable.ic_end, activity.colorPrimary);
                            if (startLocationDrawable != null) {
                                Bitmap startLocationtBitmap = DrawableUtils.setDrawableHeightWithKeepRatio(startLocationDrawable, activity.locationMarkerSize);
                                MarkerOptions startLocationMarkerOptions = new MarkerOptions()
                                        .position(startLocation)
                                        .icon(BitmapDescriptorFactory.fromBitmap(startLocationtBitmap))
                                        .anchor(0.5f, 1.0f);
                                activity.startLocationMarker = activity.googleMap.addMarker(startLocationMarkerOptions);
                            }
                            if (endLocationDrawable != null) {
                                Bitmap endLocationtBitmap = DrawableUtils.setDrawableHeightWithKeepRatio(endLocationDrawable, activity.locationMarkerSize);
                                MarkerOptions startLocationMarkerOptions = new MarkerOptions()
                                        .position(endLocation)
                                        .icon(BitmapDescriptorFactory.fromBitmap(endLocationtBitmap))
                                        .anchor(0.0f, 1.0f);
                                activity.endLocationMarker = activity.googleMap.addMarker(startLocationMarkerOptions);
                            }

                            activity.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(journeyDetailsPathEvent.getPathBoundingBox(), 100));
                        }
                    }
                }
            }
        }
    }
}
