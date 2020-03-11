package com.vivek.githubtrending.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vivek.githubtrending.R;
import com.vivek.githubtrending.data.Resource;
import com.vivek.githubtrending.data.local.entity.GithubEntity;
import com.vivek.githubtrending.databinding.MainActivityBinding;
import com.vivek.githubtrending.ui.ViewModelFactory;
import com.vivek.githubtrending.ui.adapter.TrendingListAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private MainActivityBinding binding;
    private MainViewModel mainViewModel;
    TrendingListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initialiseViewModel();
        initialiseView();


    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.mainToolbar.toolbar);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TrendingListAdapter(getApplicationContext());
        binding.recyclerview.setAdapter(adapter);


        /* This is to handle configuration changes:
         * during configuration change, when the activity
         * is recreated, we check if the viewModel
         * contains the list data. If so, there is no
         * need to call the api or load data from cache again */
        mainViewModel.getRepositories().observe(this, new Observer<Resource<List<GithubEntity>>>() {
            @Override
            public void onChanged(Resource<List<GithubEntity>> listResource) {


                if (listResource != null) {
                    Timber.d("onChanged: status: " + listResource.status);

                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
//                                if(mRecipeListViewModel.getPageNumber() > 1){
//                                    mAdapter.displayLoading();
//                                }
//                                else{
//                                    mAdapter.displayOnlyLoading();
//                                }
//                                break;
                            }

                            case ERROR: {
                                Timber.e("onChanged: cannot refresh the cache.");
                                Timber.e("onChanged: ERROR message: %s", listResource.message);
                                Timber.e("onChanged: status: ERROR, #recipes: %s", listResource.data.size());
//                                mAdapter.hideLoading();
//                                mAdapter.setRecipes(listResource.data);
//                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
//
//                                if(listResource.message.equals(QUERY_EXHAUSTED)){
//                                    mAdapter.setQueryExhausted();
//                                }
//                                break;
                            }

                            case SUCCESS: {
                                Timber.d("onChanged: cache has been refreshed.");
                                Timber.d("onChanged: status: SUCCESS, #Recipes: %s", listResource.data.size());
//                                mAdapter.hideLoading();
//                                mAdapter.setRecipes(listResource.data);
                                break;
                            }
                        }
                    }
                }

            }
        });

    }

    private void initialiseViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mainViewModel.getRepositoryListLiveData().observe(this, repositories -> {
            if (adapter.getItemCount() == 0) {
                if (!repositories.isEmpty()) {
                    displayDataView(repositories);

                } else displayEmptyView();

            } else if (!repositories.isEmpty()) displayDataView(repositories);
        });
    }


    private void displayEmptyView() {
        hideLoader();
        binding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }

    private void displayLoader() {
        binding.viewLoader.rootView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.viewLoader.rootView.setVisibility(View.GONE);
    }


    private void displayDataView(List<GithubEntity> repositories) {
        binding.viewEmpty.emptyContainer.setVisibility(View.GONE);
        adapter.setItems(repositories);
    }
}
