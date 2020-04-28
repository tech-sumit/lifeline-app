package com.easy.sumit.lifeline.ActivityHandlers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.easy.sumit.lifeline.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
