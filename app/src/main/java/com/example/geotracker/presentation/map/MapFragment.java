package com.example.geotracker.presentation.map;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geotracker.R;
import com.example.geotracker.presentation.base.BaseFragment;
import com.example.geotracker.presentation.base.BaseFragmentActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MapViewModel mapViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mapViewModel = ViewModelProviders.of((BaseFragmentActivity)context, this.viewModelFactory).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

}
