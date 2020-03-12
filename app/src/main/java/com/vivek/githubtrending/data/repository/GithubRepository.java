package com.vivek.githubtrending.data.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubTrendingApiService githubApiService;
    private final Executor executor;

    public GithubRepository(GithubDao githubDao, GithubTrendingApiService githubApiService, Executor executor) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
        this.executor = executor;
    }


    public LiveData<List<GithubEntity>> getLiveRepoList() {


        refreshData(); // try to refresh data if possible from Github Api
        return githubDao.getTrendingRepository(); // return a LiveData directly from the database.
    }

    private void refreshData() {

        executor.execute(() -> {

            githubApiService.fetchTrendingRepositoriesRest().enqueue(new Callback<List<GithubEntity>>() {
                @Override
                public void onResponse(@NonNull Call<List<GithubEntity>> call, @NonNull Response<List<GithubEntity>> response) {

                    Timber.e("DATA REFRESHED FROM NETWORK");
                    executor.execute(() -> {
                        List<GithubEntity> entityList = response.body();

                        if (entityList != null) {
                            for (int i = 0; i < entityList.size(); i++) {
                                entityList.get(i).setLastRefresh(new Date());
                            }

                            githubDao.insertRepositories(entityList);
                        }

                    });
                }

                @Override
                public void onFailure(@NonNull Call<List<GithubEntity>> call, @NonNull Throwable t) {
                    Timber.d("error occured %s", t.getLocalizedMessage());
                }
            });


        });
    }


//    private Date getMaxRefreshTime(Date currentDate) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(currentDate);
//        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
//        return cal.getTime();
//    }

}
