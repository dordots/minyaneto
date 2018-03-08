package com.app.minyaneto_android.monitoring;


import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.testfairy.TestFairy;

import timber.log.Timber;

public class MinyanetoDebugTree extends Timber.DebugTree {

  @Override
  protected void log(int priority, String tag, String message, Throwable t) {
    super.log(priority, tag, message, t);

    if (t == null) {
      Crashlytics.log(String.format("%s: %s", tag, message));
      TestFairy.log(tag, message);
    } else {
      String stackTraceString = Log.getStackTraceString(t);
      Crashlytics.log(String.format("%s: %s - %s", tag, message, stackTraceString));
      TestFairy.log(tag, String.format("%s - %s", tag, stackTraceString));
    }
  }
}
