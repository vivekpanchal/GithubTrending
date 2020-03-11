package com.vivek.githubtrending.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.vivek.githubtrending.data.local.entity.GithubEntity;

import java.util.ArrayList;
import java.util.List;

public class GithubApiResponse implements Parcelable {

    public GithubApiResponse() {
        this.items = new ArrayList<>();
    }

    private List<GithubEntity> items;


    protected GithubApiResponse(Parcel in) {
        items = in.createTypedArrayList(GithubEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GithubApiResponse> CREATOR = new Creator<GithubApiResponse>() {
        @Override
        public GithubApiResponse createFromParcel(Parcel in) {
            return new GithubApiResponse(in);
        }

        @Override
        public GithubApiResponse[] newArray(int size) {
            return new GithubApiResponse[size];
        }
    };

    public List<GithubEntity> getItems() {
        return items;
    }

    public void setItems(List<GithubEntity> items) {
        this.items = items;
    }


}
