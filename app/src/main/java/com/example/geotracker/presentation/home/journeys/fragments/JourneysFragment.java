package com.example.geotracker.presentation.home.journeys.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geotracker.ApplicationContext;
import com.example.geotracker.R;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.presentation.base.BaseFragment;
import com.example.geotracker.presentation.base.BaseFragmentActivity;
import com.example.geotracker.presentation.home.MainViewModel;
import com.example.geotracker.presentation.home.journeys.JourneysViewModel;
import com.example.geotracker.presentation.home.journeys.adapters.JourneyRecyclerAdapter;
import com.example.geotracker.presentation.journeys.adapters.JourneyClickListener;
import com.example.geotracker.presentation.journeys.adapters.datamodel.JourneyItem;
import com.example.geotracker.presentation.journeys.events.JourneysEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Simple {@link android.support.v4.app.Fragment} implementation meant to provide the user with a visual representation of currently and previously recorded
 * journeys. Such representation is provided through the usage of a {@link RecyclerView}, constructed as a grid. Each item displays both a journey's
 * name and its start datetime in a user-readable way.
 * This fragment uses both its specific viewmodel implementation {@link JourneysViewModel} and a {@link MainViewModel}. As such, it's able to listen to MainActivity
 * specific events, as well as keeping it's own events' subset separate from the rest of the Views, thus reducing the complexity of ViewModel classes.
 */
public class JourneysFragment extends BaseFragment {
    public static final String TAG = JourneysFragment.class.getCanonicalName() + ".TAG";
    private static final int GRID_SPANS_COUNT = 2;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    @ApplicationContext
    Context applicationContext;

    @BindView(R.id.fragment_journeys_rv)
    RecyclerView fragmentJourneysRv;
    Unbinder unbinder;
    @BindView(R.id.fragment_journeys_toolbar)
    Toolbar fragmentJourneysToolbar;
    @BindView(R.id.fragment_journeys_appbar)
    AppBarLayout appBarLayout;


    private JourneysViewModel viewModel;
    private MainViewModel mainViewModel;

    public static JourneysFragment newInstance() {
        Bundle args = new Bundle();
        JourneysFragment fragment = new JourneysFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public JourneysFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.viewModel = ViewModelProviders.of((BaseFragmentActivity)context, this.viewModelFactory).get(JourneysViewModel.class);
        this.mainViewModel = ViewModelProviders.of((BaseFragmentActivity)context, this.viewModelFactory).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journeys, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fragmentJourneysToolbar.setTitle(R.string.journeys_list_title);
        this.fragmentJourneysToolbar.setTitleTextColor(ContextCompat.getColor(this.applicationContext, android.R.color.white));
        ViewCompat.setElevation(this.appBarLayout, getResources().getDimensionPixelSize(R.dimen.bars_elevation));
        if (getActivity() != null) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), GRID_SPANS_COUNT);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            this.fragmentJourneysRv.setLayoutManager(layoutManager);
            this.fragmentJourneysRv.setItemAnimator(itemAnimator);
            this.fragmentJourneysRv.setHasFixedSize(true);
            List<JourneyItem> emptyDataset = new ArrayList<>();
            RecyclerView.Adapter adapter = new JourneyRecyclerAdapter(emptyDataset, new JourneyItemClickListener());
            this.fragmentJourneysRv.setAdapter(adapter);
            this.viewModel.getJourneysEventsObservableStream()
                    .observe(this, new JourneyListUpdateObserver(this));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class JourneyListUpdateObserver implements Observer<JourneysEvent> {
        private WeakReference<JourneysFragment> fragmentWeakReference;

        JourneyListUpdateObserver(@NonNull JourneysFragment journeysFragment) {
            this.fragmentWeakReference = new WeakReference<>(journeysFragment);
        }

        @Override
        public void onChanged(@Nullable JourneysEvent journeysEvent) {
            JourneysFragment journeysFragment = this.fragmentWeakReference.get();
            if (journeysFragment != null && journeysEvent != null) {
                switch (journeysEvent.getType()) {
                    case TYPE_JOURNEYS_LIST_CHANGED:
                        List<VisibleJourney> visibleJourneys = journeysEvent.getLatestAvailableJourneys();
                        List<JourneyItem> listDataset = new ArrayList<>(visibleJourneys.size());
                        for (VisibleJourney visibleJourney : visibleJourneys) {
                            listDataset.add(new JourneyItem(visibleJourney.getIdentifier(), visibleJourney.getTitle(), visibleJourney.getStartedAtUTCDateTimeIso(), !visibleJourney.isComplete()));
                        }
                        RecyclerView.Adapter adapter = journeysFragment.fragmentJourneysRv.getAdapter();
                        if (adapter instanceof JourneyRecyclerAdapter) {
                            JourneyRecyclerAdapter castAdapter = (JourneyRecyclerAdapter) adapter;
                            castAdapter.newDataset(listDataset);
                        }
                        break;
                }
            }
        }
    }

    private class JourneyItemClickListener implements JourneyClickListener {

        @Override
        public void onJourneyItemClicked(long clickedItemId, boolean isActive) {
            JourneysFragment.this.mainViewModel.onJourneyItemClicked(clickedItemId, isActive);
        }
    }
}
