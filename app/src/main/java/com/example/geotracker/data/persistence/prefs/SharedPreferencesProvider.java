package com.example.geotracker.data.persistence.prefs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface SharedPreferencesProvider {
    Maybe<String> getSingleStringPrefValue(@NonNull String prefKey);
    Maybe<Boolean> getSingleBooleanPrefValue(@NonNull String prefKey);

    Flowable<String> getRefreshingStringPrefValue(@NonNull String prefKey);
    Flowable<Boolean> getRefreshingBooleanPrefValue(@NonNull String prefKey);

    Completable putStringPrefValue(@NonNull String prefKey, @Nullable String prefValue);
    Completable putBooleanPrefValue(@NonNull String prefKey, boolean prefValue);
}
