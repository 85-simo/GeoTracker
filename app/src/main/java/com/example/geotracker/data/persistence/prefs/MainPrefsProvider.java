package com.example.geotracker.data.persistence.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Documented;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class MainPrefsProvider implements SharedPreferencesProvider{
    private SharedPreferences sharedPreferences;

    @Inject
    MainPrefsProvider(@NonNull SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    @Override
    public Maybe<String> getSingleStringPrefValue(@NonNull String prefKey) {
        return Maybe.create(emitter -> {
            try {
                String prefValue = MainPrefsProvider.this.sharedPreferences.getString(prefKey, null);
                if (prefValue != null) {
                    emitter.onSuccess(prefValue);
                }
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Maybe<Boolean> getSingleBooleanPrefValue(@NonNull String prefKey) {
        return Maybe.create(emitter -> {
            try {
                boolean prefValue = MainPrefsProvider.this.sharedPreferences.getBoolean(prefKey, false);
                emitter.onSuccess(prefValue);
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Flowable<String> getRefreshingStringPrefValue(@NonNull String prefKey) {
        return Flowable.create(emitter -> {
            try {
                String initialPrefValue = MainPrefsProvider.this.sharedPreferences.getString(prefKey, null);
                if (initialPrefValue != null) {
                    emitter.onNext(initialPrefValue);
                }
                SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
                    if (key.equals(prefKey)) {
                        String prefValue = sharedPreferences.getString(key, null);
                        if (prefValue != null) {
                            emitter.onNext(prefValue);
                        }
                    }
                };

                MainPrefsProvider.this.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
                emitter.setCancellable(() -> MainPrefsProvider.this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> getRefreshingBooleanPrefValue(@NonNull String prefKey) {
        return Flowable.create(emitter -> {
            try {
                emitter.onNext(MainPrefsProvider.this.sharedPreferences.getBoolean(prefKey, false));
                SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
                    if (key.equals(prefKey)) {
                        emitter.onNext(sharedPreferences.getBoolean(prefKey, false));
                    }
                };
                MainPrefsProvider.this.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
                emitter.setCancellable(() -> MainPrefsProvider.this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Completable putStringPrefValue(@NonNull String prefKey, @Nullable String prefValue) {
        return Completable.create(emitter -> {
            try {
                SharedPreferences.Editor editor = MainPrefsProvider.this.sharedPreferences.edit();
                editor.putString(prefKey, prefValue);
                editor.apply();
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Completable putBooleanPrefValue(@NonNull String prefKey, boolean prefValue) {
        return Completable.create(emitter -> {
            try {
                SharedPreferences.Editor editor = MainPrefsProvider.this.sharedPreferences.edit();
                editor.putBoolean(prefKey, prefValue);
                editor.apply();
                emitter.onComplete();
            }
            catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
