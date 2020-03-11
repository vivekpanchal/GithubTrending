package com.vivek.githubtrending.data.remote.model;

import com.vivek.githubtrending.data.local.entity.GithubEntity;

import java.util.ArrayList;
import java.util.List;

public class GithubApiResponse {

    public GithubApiResponse() {
        this.items = new ArrayList<>();
    }

    public GithubApiResponse(List<GithubEntity> items) {
        this.items = items;
    }

    private List<GithubEntity> items;


    public List<GithubEntity> getItems() {
        return items;
    }

    public void setItems(List<GithubEntity> items) {
        this.items = items;
    }
}
