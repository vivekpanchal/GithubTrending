package com.vivek.githubtrending.data.repository;



import androidx.annotation.NonNull;

import com.vivek.githubtrending.data.NetworkBoundResource;
import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.data.remote.model.GithubApiResponse;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;



@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubTrendingApiService githubApiService;

    public GithubRepository(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
    }

    public Observable<Resource<List<GithubEntity>>> getRepositories() {
        return new NetworkBoundResource<List<GithubEntity>, GithubApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull GithubApiResponse item) {
                List<GithubEntity> repositories = item.getItems();

                githubDao.insertRepositories(repositories);
            }

            @Override
            protected boolean shouldFetch() {
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
            protected Observable<Resource<GithubApiResponse>> createCall() {
                return githubApiService.fetchTrendingRepositories()
                        .flatMap(response ->
                                Observable.just(response.isSuccessful()
                                        ? Resource.success(response.body())
                                        : Resource.error("", new GithubApiResponse())));
            }

        }.getAsObservable();
    }
}
