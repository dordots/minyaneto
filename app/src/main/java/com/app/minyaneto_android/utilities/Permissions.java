package com.app.minyaneto_android.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.app.minyaneto_android.R;


/**
 * Created by david vardi
 */

public class Permissions {

  public static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
  public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 101;
  public static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 102;
  public static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 103;

  public static void requestPermissionForCamera(Activity activity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
      Toast.makeText(activity, R.string.camera_permission_toast, Toast.LENGTH_LONG).show();
    } else {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
          CAMERA_PERMISSION_REQUEST_CODE);
    }
  }


  public static void requestPermissionForReadStorage(Activity activity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
        Manifest.permission.READ_EXTERNAL_STORAGE)) {
      Toast.makeText(activity, R.string.storage_permission_toast, Toast.LENGTH_LONG).show();
    } else {
      ActivityCompat
          .requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
              READ_STORAGE_PERMISSION_REQUEST_CODE);
    }
  }


  public static void requestPermissionForWriteStorage(Activity activity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      Toast.makeText(activity, R.string.storage_permission_toast, Toast.LENGTH_LONG).show();
    } else {
      ActivityCompat
          .requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
              WRITE_STORAGE_PERMISSION_REQUEST_CODE);
    }
  }

  public static void requestPermissionForGPS(Activity activity) {
    if (ActivityCompat
        .shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
      Toast.makeText(activity, R.string.location_permission_toast, Toast.LENGTH_LONG).show();
    } else {
      ActivityCompat
          .requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
              ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
    }
  }


  public static boolean checkPermissionForCamera(Activity activity) {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

      return true;

    }

    int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
    return result == PackageManager.PERMISSION_GRANTED;
  }


  public static boolean checkPermissionForReadStorage(Activity activity) {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

      return true;

    }

    int result = ContextCompat
        .checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    return result == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean checkPermissionForGPS(Activity activity) {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

      return true;

    }

    int result = ContextCompat
        .checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    return result == PackageManager.PERMISSION_GRANTED;
  }


  public static boolean checkPermissionForWriteStorage(Activity activity) {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

      return true;

    }

    int result = ContextCompat
        .checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    return result == PackageManager.PERMISSION_GRANTED;
  }


}
