package com.example.geotracker.presentation.map;

import android.arch.lifecycle.ViewModel;

import com.example.geotracker.presentation.PerActivity;

import javax.inject.Inject;

@PerActivity
public class MapViewModel extends ViewModel {

    @Inject
    MapViewModel() {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
