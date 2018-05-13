package com.example.geotracker.data.persistence.prefs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Documented;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface SharedPreferencesProvider {
    public static final String PREF_KEY_TRACKING_ACTIVE = "tracking";

    @Documented
    @StringDef({PREF_KEY_TRACKING_ACTIVE})
    public @interface PrefKey {}

    Maybe<String> getSingleStringPrefValue(@NonNull @PrefKey String prefKey);
    Maybe<Boolean> getSingleBooleanPrefValue(@NonNull @PrefKey String prefKey);

    Flowable<String> getRefreshingStringPrefValue(@NonNull @PrefKey String prefKey);
    Flowable<Boolean> getRefreshingBooleanPrefValue(@NonNull @PrefKey String prefKey);

    Completable putStringPrefValue(@NonNull @PrefKey String prefKey, @Nullable String prefValue);
    Completable putBooleanPrefValue(@NonNull @PrefKey String prefKey, boolean prefValue);
}
