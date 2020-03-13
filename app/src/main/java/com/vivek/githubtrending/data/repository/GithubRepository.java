package com.vivek.githubtrending.data.repository;


import androidx.annotation.NonNull;

import com.vivek.githubtrending.data.NetworkBoundResource;
import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.CallTimeOutEntity;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.util.ApplicationConstants;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import timber.log.Timber;


@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubTrendingApiService githubApiService;

    public GithubRepository(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
    }

    public Observable<Resource<List<GithubEntity>>> getRepositories() {
        return new NetworkBoundResource<List<GithubEntity>, List<GithubEntity>>() {

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
            protected boolean shouldFetch() {
                return isRefreshDataRequired();
            }

            @NonNull
            @Override
            protected Flowable<List<GithubEntity>> loadFromDb() {
                List<GithubEntity> repositories = githubDao.getTrendingRepository();
                if (repositories != null) {
                    Timber.d("loading from Db called  current repository in db %s", repositories.size());
                }
                return (repositories == null || repositories.isEmpty()) ?
                        Flowable.empty() : Flowable.just(repositories);
            }

            @NonNull
            @Override
            protected Observable<Resource<List<GithubEntity>>> createCall() {
                Timber.d("Creating call to fetch the data ");
                //flatMap is used to convert the list of items into observable
                return githubApiService.fetchTrendingRepositories().flatMap(
                        listResponse -> {
                            if (listResponse.isSuccessful()) {
                                if (listResponse.body() != null) {
                                    return Observable.just(Resource.success(listResponse.body()));
                                } else {
                                    return Observable.just(Resource.error("NO data fetched", null));
                                }
                            } else {
                                return Observable.just(Resource.error("Error fetching trending repositories", null));
                            }
                        }
                );
            }

        }.getAsObservable();
    }


    public Observable<Resource<List<GithubEntity>>> getRepositoriesForceUpdate() {
        return new NetworkBoundResource<List<GithubEntity>, List<GithubEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<GithubEntity> item) {

                if (!item.isEmpty()) {
                    Timber.d("save call request called saving data in the database ");
                    githubDao.insertRepositories(item);


                    //adding the success transaction int the db
                    CallTimeOutEntity timeOutEntity = new CallTimeOutEntity();
                    timeOutEntity.setLastRefreshTimeStamp((int) (System.currentTimeMillis() / 1000));
                    timeOutEntity.setNetworkCall(true);
                    githubDao.insertNetworkCallTime(timeOutEntity);
                }

            }

            /**
             * force refresh data  true
             */
            @Override
            protected boolean shouldFetch() {
                Timber.d("force fully updating the data");
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<GithubEntity>> loadFromDb() {
                List<GithubEntity> repositories = githubDao.getTrendingRepository();
                return (repositories == null || repositories.isEmpty()) ?
                        Flowable.empty() : Flowable.just(repositories);
            }

            @NonNull
            @Override
            protected Observable<Resource<List<GithubEntity>>> createCall() {
                //flatMap is used to convert the list of items into observable
                return githubApiService.fetchTrendingRepositories().flatMap(
                        listResponse -> {
                            if (listResponse.isSuccessful()) {
                                if (listResponse.body() != null) {
                                    return Observable.just(Resource.success(listResponse.body()));
                                } else {
                                    return Observable.just(Resource.error("NO data fetched", null));
                                }
                            } else {
                                return Observable.just(Resource.error("Error fetching trending repositories", null));
                            }
                        }
                );
            }

        }.getAsObservable();
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
