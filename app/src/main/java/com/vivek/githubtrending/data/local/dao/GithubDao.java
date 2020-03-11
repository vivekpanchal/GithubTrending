package com.vivek.githubtrending.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vivek.githubtrending.data.local.entity.GithubEntity;

import java.util.List;

@Dao
public interface GithubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepositories(List<GithubEntity> githubEntities);

    @Query("SELECT * FROM `GithubEntity`")
    LiveData<List<GithubEntity>> getTrendingRepository();
}
