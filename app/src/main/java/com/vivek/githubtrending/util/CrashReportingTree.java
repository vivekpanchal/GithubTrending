package com.vivek.githubtrending.util;

import android.util.Log;

import timber.log.Timber;

public class CrashReportingTree extends Timber.Tree {
    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";

    @Override
    protected void log(int priority, String tag, String message, Throwable throwable) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {

            return;
        }

        Throwable t = throwable != null
                ? throwable
                : new Exception(message);


    }
}

