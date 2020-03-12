package com.vivek.githubtrending.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vivek.githubtrending.data.local.entity.CallTimeOutEntity;
import com.vivek.githubtrending.data.local.entity.GithubEntity;

import java.util.List;

@Dao
public interface GithubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepositories(List<GithubEntity> githubEntities);

    @Query("SELECT * FROM `GithubEntity`")
    List<GithubEntity> getTrendingRepository();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNetworkCallTime(CallTimeOutEntity timeOutEntity);

    @Query("SELECT * FROM CallTimeOutEntity  ORDER BY id DESC LIMIT 1")
    CallTimeOutEntity getLatestTimeout();


}
