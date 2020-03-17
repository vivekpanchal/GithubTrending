package com.vivek.githubtrending.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.SingleLiveEvent;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.data.remote.api.GithubTrendingApiService;
import com.vivek.githubtrending.data.repository.GithubRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainViewModel extends ViewModel {


    private GithubRepository repository;
    public static final String NO_ITEM_FOUND = "No item found.";
    private MediatorLiveData<Resource<List<GithubEntity>>> repositories = new MediatorLiveData<>();
    private SingleLiveEvent<List<GithubEntity>> repoListLiveData = new SingleLiveEvent<>();

    @Inject
    public MainViewModel(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        repository = new GithubRepository(githubDao, githubApiService);
        fetchRepositories();
    }


    public void fetchRepositories() {
        final LiveData<Resource<List<GithubEntity>>> repositorySource = repository.getRepositories();
        repositories.addSource(repositorySource, new Observer<Resource<List<GithubEntity>>>() {
            @Override
            public void onChanged(Resource<List<GithubEntity>> listResource) {

                if (listResource != null) {
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Timber.d("onChanged: %s", listResource.data);

                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Timber.d("onChanged: Empty data...");
                                repositories.setValue(
                                        new Resource<List<GithubEntity>>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                NO_ITEM_FOUND
                                        )
                                );
                            }
                        }
                        repositories.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        repositories.removeSource(repositorySource);
                    }
                    repositories.setValue(listResource);
                } else {
                    repositories.removeSource(repositorySource);
                }
            }

        });

    }


    public void forceFetchRepo() {
        final LiveData<Resource<List<GithubEntity>>> repositorySource = repository.forceFetchData();
        repositories.addSource(repositorySource, new Observer<Resource<List<GithubEntity>>>() {
            @Override
            public void onChanged(Resource<List<GithubEntity>> listResource) {

                if (listResource != null) {
                    if (listResource.status == Resource.Status.SUCCESS) {
                        Timber.d("onChanged: %s", listResource.data);

                        if (listResource.data != null) {
                            if (listResource.data.size() == 0) {
                                Timber.d("onChanged: Empty data...");
                                repositories.setValue(
                                        new Resource<List<GithubEntity>>(
                                                Resource.Status.ERROR,
                                                listResource.data,
                                                NO_ITEM_FOUND
                                        )
                                );
                            }
                        }
                        repositories.removeSource(repositorySource);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        repositories.removeSource(repositorySource);
                    }
                    repositories.setValue(listResource);
                } else {
                    repositories.removeSource(repositorySource);
                }
            }

        });
    }

    public LiveData<Resource<List<GithubEntity>>> getRepositories() {
        return repositories;
    }

    public SingleLiveEvent<List<GithubEntity>> getRepositoryListLiveData() {
        return repoListLiveData;
    }

}
