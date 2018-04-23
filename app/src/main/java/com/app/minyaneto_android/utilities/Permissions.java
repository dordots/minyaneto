package com.app.minyaneto_android.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;


public class Permissions {

  public static boolean checkPermissionForGPS(Activity activity) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return true;
    }
    int result = ContextCompat
        .checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    return result == PackageManager.PERMISSION_GRANTED;
  }
}
