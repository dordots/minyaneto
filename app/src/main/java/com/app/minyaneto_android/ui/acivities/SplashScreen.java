package com.app.minyaneto_android.ui.acivities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.utilities.user.Alerts;
import com.app.minyaneto_android.utilities.user.ApplicationManager;

public class SplashScreen extends AppCompatActivity implements Alerts.OnCancelDialogListener {

    private CountDownTimer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

      //  init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        if (ApplicationManager.isNetworkAvailable(this)) {

            startNextActivity();

        } else {

            Alerts.noInternet(this, this);

        }

    }

    private void startNextActivity() {

        int SPLASH_SCREEN_TIME_IN_MILI = 1000;

        splashTimer = new CountDownTimer(SPLASH_SCREEN_TIME_IN_MILI, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

                finish();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (splashTimer != null)

            splashTimer.cancel();
    }


    @Override
    public void onCancelAlertDialog() {

        finish();
    }

    @Override
    public void onClickOkAlertDialog() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Alerts.ACTION_CODE_OPEN_INTERNET_SETTINGS) {

            init();

        }

    }
}