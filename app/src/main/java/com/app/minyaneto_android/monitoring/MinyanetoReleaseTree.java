package com.app.minyaneto_android.monitoring;


import android.util.Log;
import com.crashlytics.android.Crashlytics;
import timber.log.Timber;

public class MinyanetoReleaseTree extends Timber.Tree {

  @Override
  protected void log(int priority, String tag, String message, Throwable t) {
    //log to crashlytics only in release
    if (t == null) {
      Crashlytics.log(String.format("%s: %s", tag, message));
    } else {
      Crashlytics.log(String.format("%s: %s - %s", tag, message, Log.getStackTraceString(t)));
    }
  }
}
