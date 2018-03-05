package com.app.minyaneto_android.utilities.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.app.minyaneto_android.R;


/**
 * Created by david vardi
 */
public class Alerts {

    public static final int ACTION_CODE_OPEN_GPS_SETTINGS = 234 ;
    public static final int ACTION_CODE_OPEN_INTERNET_SETTINGS = 235;

    public static final String TAG = Alerts.class.getSimpleName();


    public static boolean noInternetAlertShowing = false;
    public static boolean noGPSAlertShowing = false ;


    public static void noInternet(final Activity activity, final OnCancelDialogListener onCancelDialogListener) {
        if (!noInternetAlertShowing) {
            noInternetAlertShowing = true;
            new AlertDialog.Builder(activity)

                    .setTitle(activity.getResources().getString(R.string.no_internet_connection))
                    .setMessage(activity.getResources().getString(R.string.message_alert_no_interner))
                    .setPositiveButton(activity.getResources().getString(R.string.open_internet_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            noInternetAlertShowing = false;
                            Intent chooserIntent = Intent.createChooser(new Intent(Settings.ACTION_WIRELESS_SETTINGS), activity.getResources().getString(R.string.select_settings));
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{new Intent(Settings.ACTION_WIFI_SETTINGS)});
                            // intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                            activity.startActivityForResult(chooserIntent, ACTION_CODE_OPEN_INTERNET_SETTINGS);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noInternetAlertShowing = false;
                            onCancelDialogListener.onCancelAlertDialog();
                            dialog.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();

        }
    }



    public static void noGPS(final Activity context , final OnCancelDialogListener onCancelDialogListener) {
        if (!noGPSAlertShowing) {
            noGPSAlertShowing  = true ;
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.no_gps_connection))
                    .setMessage(context.getResources().getString(R.string.message_alert_no_gps))
                    .setPositiveButton(context.getResources().getString(R.string.open_internet_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noGPSAlertShowing = false;
                            dialog.cancel();
                            context.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ACTION_CODE_OPEN_GPS_SETTINGS);
                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.close_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            noGPSAlertShowing = false;
                            onCancelDialogListener.onCancelAlertDialog();
                            dialog.cancel();

                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }





    public interface OnCancelDialogListener {
        void onCancelAlertDialog();
        void onClickOkAlertDialog();
    }

}
