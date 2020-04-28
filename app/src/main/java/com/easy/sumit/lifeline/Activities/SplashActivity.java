package com.easy.sumit.lifeline.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.easy.sumit.lifeline.R;

public class SplashActivity extends AppCompatActivity {
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref = getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        String login_status=pref.getString("login_status",null);
        final int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(() -> {
            if (login_status != null) {
                i = new Intent(SplashActivity.this, MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("last_activity","SplashActivity.java");
                i.putExtras(bundle);
            }
            else{
                i = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(i);
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
