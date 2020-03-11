package com.vivek.githubtrending.di.module;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.vivek.githubtrending.data.local.AppDatabase;
import com.vivek.githubtrending.data.local.dao.GithubDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "Github.db")
                .build();
    }

    @Provides
    @Singleton
    GithubDao provideGithubDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.githubDao();
    }
}
