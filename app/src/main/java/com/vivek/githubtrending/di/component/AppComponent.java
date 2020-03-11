package com.vivek.githubtrending.di.component;

import android.app.Application;

import com.vivek.githubtrending.BaseApplication;
import com.vivek.githubtrending.di.module.ActivityModule;
import com.vivek.githubtrending.di.module.ApiModule;
import com.vivek.githubtrending.di.module.DbModule;
import com.vivek.githubtrending.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Component(modules = {
        ApiModule.class,
        DbModule.class,
        ViewModelModule.class,
        ActivityModule.class,
        AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    void inject(BaseApplication baseApplication);
}
