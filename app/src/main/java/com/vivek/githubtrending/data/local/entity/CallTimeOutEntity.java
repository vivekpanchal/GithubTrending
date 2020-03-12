package com.vivek.githubtrending.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CallTimeOutEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean networkCall;
    private int lastRefreshTimeStamp;


    public CallTimeOutEntity() {
    }

    public CallTimeOutEntity(boolean networkCall, int lastRefreshTimeStamp) {
        this.networkCall = networkCall;
        this.lastRefreshTimeStamp = lastRefreshTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNetworkCall() {
        return networkCall;
    }

    public void setNetworkCall(boolean networkCall) {
        this.networkCall = networkCall;
    }

    public int getLastRefreshTimeStamp() {
        return lastRefreshTimeStamp;
    }

    public void setLastRefreshTimeStamp(int lastRefreshTimeStamp) {
        this.lastRefreshTimeStamp = lastRefreshTimeStamp;
    }
}
