package com.vivek.githubtrending.data.remote.api;


import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.model.GithubApiResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface GithubTrendingApiService {

    @GET("/repositories")
    Observable<Response<List<GithubEntity>>> fetchTrendingRepositories();
    // you should always return object matching the json object
    // the link https://github-trending-api.now.sh/repositories return a list of GithubEntity

}
