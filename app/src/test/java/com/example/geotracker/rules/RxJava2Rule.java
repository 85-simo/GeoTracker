package com.example.geotracker.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxJava2Rule implements TestRule {



    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                setupRxSchedulers();
                base.evaluate();
                resetRxSchedulers();
            }
        };
    }

    private static void setupRxSchedulers() {
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    private static void resetRxSchedulers() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.computation());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.io());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.newThread());
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> Schedulers.single());
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> AndroidSchedulers.mainThread());
    }
}
