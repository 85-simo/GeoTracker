package com.example.geotracker.presentation.map;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.widget.FrameLayout;

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.map_activity_bnv)
    BottomNavigationView mapActivityBnv;
    @BindView(R.id.map_activity_content_fl)
    FrameLayout mapActivityContentFl;

    private MapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(MapViewModel.class);

    }
}
