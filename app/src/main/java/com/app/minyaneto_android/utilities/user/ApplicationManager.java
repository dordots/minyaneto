package com.app.minyaneto_android.utilities.user;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * manage all function generic for app.
 */
public class ApplicationManager {

  /**
   * Checks if the device has Internet connection.
   *
   * @return <code>true</code> if the phone is connected to the Internet.
   */
  public static boolean isNetworkAvailable(Context context) {

    ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

    return activeNetworkInfo != null && activeNetworkInfo.isConnected();

  }
}
