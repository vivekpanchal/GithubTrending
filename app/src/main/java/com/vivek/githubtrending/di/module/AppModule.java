package com.vivek.githubtrending.di.module;

import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.data.repository.GithubRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    GithubRepository providesRepository(GithubDao dao, GithubTrendingApiService trendingApiService, Executor executor) {
        return new GithubRepository(dao, trendingApiService, executor);
    }
}
