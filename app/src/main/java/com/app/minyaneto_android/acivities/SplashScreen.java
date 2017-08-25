package com.app.minyaneto_android.acivities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.acivities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_SCREEN_TIME_IN_MILI = 3000;
    private CountDownTimer splashTimer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(230, 0, 172)));
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
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
        splashTimer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        splashTimer.start();
    }
}