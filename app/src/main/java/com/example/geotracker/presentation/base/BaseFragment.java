package com.example.geotracker.presentation.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.android.support.AndroidSupportInjection;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
