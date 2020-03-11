package com.vivek.githubtrending.di.module;

import com.vivek.githubtrending.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity ContributeMainActivity();
}