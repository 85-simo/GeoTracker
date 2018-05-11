package com.example.geotracker.rules;

import android.content.Context;
import android.content.res.Resources;

import net.danlew.android.joda.JodaTimeAndroid;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JodaTimeRule implements TestRule {

    public JodaTimeRule() {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);
        JodaTimeAndroid.init(context);
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }
}
