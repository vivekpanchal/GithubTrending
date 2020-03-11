package com.vivek.githubtrending;

import android.app.Activity;
import android.app.Application;

import com.vivek.githubtrending.di.component.DaggerAppComponent;
import com.vivek.githubtrending.util.CrashReportingTree;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class BaseApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);


        Timber.plant(BuildConfig.DEBUG
                ? new Timber.DebugTree()
                : new CrashReportingTree());
    }
}
