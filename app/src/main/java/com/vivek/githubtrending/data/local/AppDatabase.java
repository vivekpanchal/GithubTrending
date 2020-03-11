package com.vivek.githubtrending.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.GithubEntity;


@Database(entities = {GithubEntity.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    public abstract GithubDao githubDao();
}
