package com.example.geotracker.presentation.home.map.events;

import android.app.Activity;
import android.os.Bundle;

public class NavigationEvent {
    public enum Type {
        TYPE_ACTIVITY
    }

    private Type type;
    private Class<? extends Activity> activityClass;
    private Bundle extras;

    public NavigationEvent(Type type, Class<? extends Activity> activityClass, Bundle extras) {
        this.type = type;
        this.activityClass = activityClass;
        this.extras = extras;
    }

    public Type getType() {
        return type;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public Bundle getExtras() {
        return extras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationEvent that = (NavigationEvent) o;

        if (getType() != that.getType()) return false;
        if (getActivityClass() != null ? !getActivityClass().equals(that.getActivityClass()) : that.getActivityClass() != null)
            return false;
        return getExtras() != null ? getExtras().equals(that.getExtras()) : that.getExtras() == null;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getActivityClass() != null ? getActivityClass().hashCode() : 0);
        result = 31 * result + (getExtras() != null ? getExtras().hashCode() : 0);
        return result;
    }
}
