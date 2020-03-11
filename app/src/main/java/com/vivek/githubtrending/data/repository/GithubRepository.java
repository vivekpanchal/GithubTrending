package com.vivek.githubtrending.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.vivek.githubtrending.data.NetworkBoundResource;
import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.data.remote.model.ApiResponse;
import com.vivek.githubtrending.data.remote.model.GithubApiResponse;
import com.vivek.githubtrending.util.AppExecutors;

import java.util.List;

import javax.inject.Singleton;


@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubTrendingApiService githubApiService;

    public GithubRepository(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
    }

    public LiveData<Resource<List<GithubEntity>>> getRepositories() {
        return new NetworkBoundResource<List<GithubEntity>, GithubApiResponse>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull GithubApiResponse item) {
                List<GithubEntity> repositories = item.getItems();

                githubDao.insertRepositories(repositories);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GithubEntity> data) {
//                Timber.d("shouldFetch: repo: " + data.toString());
//                int currentTime = (int) (System.currentTimeMillis() / 1000);
//                Timber.d("shouldFetch: current time: " + currentTime);
//                int lastRefresh = data.getTimestamp();
//                Timber.d("shouldFetch: last refresh: " + lastRefresh);
//                Timber.d("shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24) +
//                        " days since this recipe was refreshed. 30 days must elapse before refreshing. ");
//                if ((currentTime - data.getTimestamp()) >= Constants.RECIPE_REFRESH_TIME) {
//                    Timber.d("shouldFetch: SHOULD REFRESH RECIPE?! " + true);
//                    return true;
//                }
//                Timber.d("shouldFetch: SHOULD REFRESH RECIPE?! " + false);
                return true;
            }


            @NonNull
            @Override
            protected LiveData<List<GithubEntity>> loadFromDb() {
                return githubDao.getTrendingRepository();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GithubApiResponse>> createCall() {
                return githubApiService.fetchTrendingRepositories();
            }

        }.getAsLiveData();
    }
}
