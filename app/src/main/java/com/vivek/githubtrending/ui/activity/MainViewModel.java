package com.vivek.githubtrending.ui.activity;

import android.annotation.SuppressLint;

import androidx.lifecycle.LifecycleOwner;
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

    private MediatorLiveData<Resource<List<GithubEntity>>> repositories = new MediatorLiveData<>();
    private SingleLiveEvent<List<GithubEntity>> repoListLiveData = new SingleLiveEvent<>();

    @Inject
    public MainViewModel(GithubDao githubDao, GithubTrendingApiService githubApiService) {
        repository = new GithubRepository(githubDao, githubApiService);
    }

//
//    @SuppressLint("CheckResult")
//    public void fetchRepositories() {
//        repository.getRepositories().observe((LifecycleOwner) this, new Observer<Resource<List<GithubEntity>>>() {
//            @Override
//            public void onChanged(Resource<List<GithubEntity>> listResource) {
//
//                if(listResource != null){
//                    Timber.d("onChanged: status: " + listResource.status);
//
//                    if(listResource.data != null){
//                        switch (listResource.status){
//                            case LOADING:{
////                                if(mRecipeListViewModel.getPageNumber() > 1){
////                                    mAdapter.displayLoading();
////                                }
////                                else{
////                                    mAdapter.displayOnlyLoading();
////                                }
////                                break;
//                            }
//
//                            case ERROR:{
//                                Timber.e("onChanged: cannot refresh the cache.");
//                                Timber.e("onChanged: ERROR message: %s", listResource.message);
//                                Timber.e("onChanged: status: ERROR, #recipes: %s", listResource.data.size());
////                                mAdapter.hideLoading();
////                                mAdapter.setRecipes(listResource.data);
////                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
////
////                                if(listResource.message.equals(QUERY_EXHAUSTED)){
////                                    mAdapter.setQueryExhausted();
////                                }
////                                break;
//                            }
//
//                            case SUCCESS:{
//                                Timber.d("onChanged: cache has been refreshed.");
//                                Timber.d("onChanged: status: SUCCESS, #Recipes: %s", listResource.data.size());
////                                mAdapter.hideLoading();
////                                mAdapter.setRecipes(listResource.data);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        });
////                .subscribe(resource -> {
////                    if (resource.isLoaded()) {
////                        repositories.addAll(resource.data);
////                        Timber.d("resource data :%s", resource.data.toString());
////
////                        getRepositoryListLiveData().postValue(resource.data);
////                    }
////                });
//    }


    public LiveData<Resource<List<GithubEntity>>> getRepositories() {
        return repositories;
    }

    public SingleLiveEvent<List<GithubEntity>> getRepositoryListLiveData() {
        return repoListLiveData;
    }

}
