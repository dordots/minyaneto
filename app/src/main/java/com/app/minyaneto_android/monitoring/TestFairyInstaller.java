package com.app.minyaneto_android.monitoring;


import android.content.Context;
import com.app.minyaneto_android.BuildConfig;
import com.testfairy.TestFairy;

public class TestFairyInstaller {

  public static void install(Context context) {
    //install test fairy only in debug for now
    if (BuildConfig.DEBUG) {
      TestFairy.begin(context, "6f4c3da555567854de9cc48b9844007c2c63eeb2");
    }
  }
}
