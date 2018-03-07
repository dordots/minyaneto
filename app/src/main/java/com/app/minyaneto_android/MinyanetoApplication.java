package com.app.minyaneto_android;


import android.app.Application;

import com.app.minyaneto_android.monitoring.MinyanetoDebugTree;

import timber.log.Timber;

public class MinyanetoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new MinyanetoDebugTree());
    }
}
