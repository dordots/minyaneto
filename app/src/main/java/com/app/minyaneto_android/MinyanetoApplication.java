package com.app.minyaneto_android;


import android.app.Application;
import com.app.minyaneto_android.monitoring.MinyanetoDebugTree;
import com.app.minyaneto_android.monitoring.MinyanetoReleaseTree;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MinyanetoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    setupLogHandler();
  }

  private void setupLogHandler() {
    Fabric.with(this, new Crashlytics());
    Timber.plant(BuildConfig.DEBUG ? new MinyanetoDebugTree() : new MinyanetoReleaseTree());
  }
}
