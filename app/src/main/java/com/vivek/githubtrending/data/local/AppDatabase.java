package com.vivek.githubtrending.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.vivek.githubtrending.data.local.converters.Converters;
import com.vivek.githubtrending.data.local.dao.GithubDao;
import com.vivek.githubtrending.data.local.entity.CallTimeOutEntity;
import com.vivek.githubtrending.data.local.entity.GithubEntity;


@Database(entities = {GithubEntity.class, CallTimeOutEntity.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GithubDao githubDao();
}
