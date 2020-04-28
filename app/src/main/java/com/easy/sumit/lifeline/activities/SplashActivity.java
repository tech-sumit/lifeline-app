package com.easy.sumit.lifeline.activities;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref = getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        final String login_status=pref.getString(Constants.LOGIN_STATUS,null);
        final int SPLASH_TIME_OUT = 3000;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( login_status != null) {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("last_activity","SplashActivity.java");
                    i.putExtras(bundle);
                }
                else{
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
