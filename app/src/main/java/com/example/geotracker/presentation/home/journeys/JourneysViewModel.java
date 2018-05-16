package com.example.geotracker.presentation.home.journeys;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.geotracker.PerFragment;
import com.example.geotracker.domain.base.RetrieveInteractor;
import com.example.geotracker.domain.dtos.VisibleJourney;
import com.example.geotracker.domain.interactors.qualifiers.AllJourneys;
import com.example.geotracker.PerActivity;
import com.example.geotracker.presentation.journeys.events.JourneysEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class JourneysViewModel extends ViewModel {
    @NonNull
    private RetrieveInteractor<Void, List<VisibleJourney>> retrieveJourneysInteractor;

    private MutableLiveData<JourneysEvent> journeysEventsObservableStream;

    private CompositeDisposable compositeDisposable;

    @Inject
    public JourneysViewModel(@NonNull @AllJourneys RetrieveInteractor<Void, List<VisibleJourney>> retrieveJourneysInteractor) {
        this.compositeDisposable = new CompositeDisposable();
        this.retrieveJourneysInteractor = retrieveJourneysInteractor;

        this.journeysEventsObservableStream = new MutableLiveData<>();

        this.compositeDisposable.add(this.retrieveJourneysInteractor
                .retrieve(null)
                .subscribe(new JourneysListConsumer()));
    }

    public LiveData<JourneysEvent> getJourneysEventsObservableStream() {
        return journeysEventsObservableStream;
    }

    @Override
    protected void onCleared() {
        this.compositeDisposable.dispose();
        super.onCleared();
    }

    private class JourneysListConsumer implements Consumer<List<VisibleJourney>> {

        @Override
        public void accept(List<VisibleJourney> visibleJourneys)  {
            Schedulers.computation().scheduleDirect(() -> {
                JourneysEvent journeysEvent = new JourneysEvent(JourneysEvent.Type.TYPE_JOURNEYS_LIST_CHANGED, visibleJourneys);
                JourneysViewModel.this.journeysEventsObservableStream.postValue(journeysEvent);
            });
        }
    }
}
