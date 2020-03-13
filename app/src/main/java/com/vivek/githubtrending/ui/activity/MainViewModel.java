package com.vivek.githubtrending.ui.activity;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;

import com.vivek.githubtrending.data.SingleLiveEvent;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.repository.GithubRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainViewModel extends ViewModel {


    private GithubRepository repository;

    private List<GithubEntity> repositories = new ArrayList<>();
    private SingleLiveEvent<List<GithubEntity>> repoListLiveData = new SingleLiveEvent<>();

    @Inject
    MainViewModel(GithubRepository GithubRepository) {
        this.repository = GithubRepository;
    }

    @SuppressLint("CheckResult")
    void fetchRepositories() {
        repository.getRepositories()
                .subscribe(resource -> {
                    if (resource.isLoaded()) {
                        repositories.addAll(resource.data);
                        Timber.d("resource data :%s", resource.data.toString());
                        getRepositoryListLiveData().postValue(resource.data);
                    }
                });
    }


    @SuppressLint("CheckResult")
    void forceFetchRepositories() {
        repository.getRepositoriesForceUpdate()
                .subscribe(resource -> {
                    if (resource.isLoaded()) {
                        repositories.addAll(resource.data);
                        Timber.d("resource data :%s", resource.data.toString());
                        getRepositoryListLiveData().postValue(resource.data);
                    }
                });
    }


    List<GithubEntity> getRepositories() {
        return repositories;
    }

    SingleLiveEvent<List<GithubEntity>> getRepositoryListLiveData() {
        return repoListLiveData;
    }

}
