package com.vivek.githubtrending.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vivek.githubtrending.R;
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


        displayLoader();


        mainViewModel.getRepositoryList().observe(this, new Observer<List<GithubEntity>>() {
            @Override
            public void onChanged(List<GithubEntity> githubEntities) {

                if (githubEntities == null || githubEntities.isEmpty()) {
                    displayEmptyView();
                } else {
                    hideLoader();
                    displayDataView(githubEntities);

                }

            }
        });


        binding.refresh.setOnRefreshListener(() -> {
            // Your code to make your refresh action
            mainViewModel.fetchRepository();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (binding.refresh.isRefreshing()) {
                    binding.refresh.setRefreshing(false);
                }
            }, 1000);
        });

    }

    private void initialiseViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
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
        Timber.d("setting up the adapter with repository of size :: %s", repositories.size());
        adapter = new TrendingListAdapter(getApplicationContext(), repositories);
        binding.recyclerview.setAdapter(adapter);

    }
}
