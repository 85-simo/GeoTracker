package com.example.geotracker.presentation.map;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.widget.FrameLayout;

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseActivity;
import com.example.geotracker.presentation.base.BaseFragmentActivity;

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

    }
}
