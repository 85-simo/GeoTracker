package com.example.geotracker.presentation.home.map.events;

import android.support.annotation.NonNull;

public class ActivityEvent {
    public enum Type {
        TYPE_TAB_SELECTED
    }

    public enum TabType {
        TAB_TYPE_MAP,
        TAB_TYPE_JOURNEYS
    }

    @NonNull
    private Type type;
    @NonNull
    private TabType tabType;

    public ActivityEvent(@NonNull Type type, @NonNull TabType tabType) {
        this.type = type;
        this.tabType = tabType;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    @NonNull
    public TabType getTabType() {
        return tabType;
    }
}
