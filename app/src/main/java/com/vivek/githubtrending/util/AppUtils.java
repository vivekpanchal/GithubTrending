package com.vivek.githubtrending.util;

import com.vivek.githubtrending.data.remote.model.GithubApiResponse;

public class AppUtils {

    public static boolean isValid(GithubApiResponse response) {
        return response.getItems() == null || response.getItems().isEmpty();
    }
}
