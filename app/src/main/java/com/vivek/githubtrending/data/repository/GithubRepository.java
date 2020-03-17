package com.vivek.githubtrending.data.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.vivek.githubtrending.data.NetworkBoundResource;
import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.CallTimeOutEntity;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.data.remote.model.ApiResponse;
import com.vivek.githubtrending.util.AppExecutors;
import com.vivek.githubtrending.util.ApplicationConstants;

import java.util.List;

import javax.inject.Singleton;

import timber.log.Timber;


@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubTrendingApiService githubApiService;

    public GithubRepository(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
    }

    public LiveData<Resource<List<GithubEntity>>> getRepositories() {
        return new NetworkBoundResource<List<GithubEntity>, List<GithubEntity>>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull List<GithubEntity> item) {

                if (!item.isEmpty()) {
                    Timber.d("save call request called saving data in the database of size %s", item.size());
                    githubDao.insertRepositories(item);

                    //adding the success transaction int the db
                    CallTimeOutEntity timeOutEntity = new CallTimeOutEntity();
                    timeOutEntity.setLastRefreshTimeStamp((int) (System.currentTimeMillis() / 1000));
                    timeOutEntity.setNetworkCall(true);
                    githubDao.insertNetworkCallTime(timeOutEntity);

                }

            }

            /**
             * only refresh data when the data is being stale for more than 2 hours until then
             * just use the cached data from the database
             * @return true false based on the condition satified
             */
            @Override
            protected boolean shouldFetch(@Nullable List<GithubEntity> data) {
                return isRefreshDataRequired();
            }


            @NonNull
            @Override
            protected LiveData<List<GithubEntity>> loadFromDb() {
                return githubDao.getTrendingRepository();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GithubEntity>>> createCall() {
                return githubApiService.fetchTrendingRepositories();
            }

        }.getAsLiveData();
    }


    public LiveData<Resource<List<GithubEntity>>> forceFetchData() {
        return new NetworkBoundResource<List<GithubEntity>, List<GithubEntity>>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull List<GithubEntity> item) {
                githubDao.insertRepositories(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GithubEntity> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<GithubEntity>> loadFromDb() {
                return githubDao.getTrendingRepository();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GithubEntity>>> createCall() {
                return githubApiService.fetchTrendingRepositories();
            }

        }.getAsLiveData();
    }


    private boolean isRefreshDataRequired() {
        CallTimeOutEntity timeOutEntity = githubDao.getLatestTimeout();
        if (timeOutEntity != null && timeOutEntity.getLastRefreshTimeStamp() != 0) {

            int currentTime = (int) (System.currentTimeMillis() / 1000);
            Timber.d("shouldFetch: current time: %s", currentTime);
            int lastRefresh = timeOutEntity.getLastRefreshTimeStamp();
            Timber.d("shouldFetch: last refresh: %s", lastRefresh);
            Timber.d("shouldFetch: it's been " + ((currentTime - lastRefresh)) +
                    "seconds since this data was refreshed. 2 hours must elapse before refreshing. ");
            if ((currentTime - lastRefresh) >= ApplicationConstants.DATA_REFRESH_TIME) {
                Timber.d("shouldFetch: SHOULD REFRESH Data?! %s", true);
                return true;
            }
            Timber.d("shouldFetch: SHOULD REFRESH Data?! %s", false);
            return false;
        } else {
            return true;
        }

    }
}
