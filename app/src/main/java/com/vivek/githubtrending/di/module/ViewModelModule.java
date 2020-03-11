package com.vivek.githubtrending.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vivek.githubtrending.di.ViewModelKey;
import com.vivek.githubtrending.ui.activity.MainViewModel;
import com.vivek.githubtrending.ui.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    protected abstract ViewModel MainViewModel(MainViewModel mainViewModel);
}