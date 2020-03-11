package com.vivek.githubtrending.data.remote.api;


import androidx.lifecycle.LiveData;

import com.vivek.githubtrending.data.remote.model.ApiResponse;
import com.vivek.githubtrending.data.remote.model.GithubApiResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface GithubTrendingApiService {

    @GET("/repositories")
    LiveData<ApiResponse<GithubApiResponse>> fetchTrendingRepositories();
}
