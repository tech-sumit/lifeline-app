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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.fragments.TermsConditionsFragment;
import com.easy.sumit.lifeline.datamodal.URLList;
import com.easy.sumit.lifeline.utils.Constants;
import com.easy.sumit.lifeline.utils.NewsService;
import com.easy.sumit.lifeline.utils.UrlUpdateService;

public class SplashActivity extends AppCompatActivity {
    private Intent i;
    private String tncStatus;
    private String login_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref = getSharedPreferences("lifeline_tnc", Context.MODE_PRIVATE);
        SharedPreferences pref1 = getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        login_status=pref1.getString(Constants.LOGIN_STATUS,null);
        tncStatus=pref.getString("tncStatus",null);
        final int SPLASH_TIME_OUT = 3000;

        URLList.setDefaultPreferences(this);
        startService(new Intent(SplashActivity.this, UrlUpdateService.class));
        //startService(new Intent(SplashActivity.this, NewsService.class));

        SharedPreferences pref3 = getSharedPreferences("lifeline_urls", Context.MODE_PRIVATE);
        Log.i("***Data Update Pref.***","Urls{"+
                "\n"+pref3.getString("call_log",null)+
                "\n"+pref3.getString("getData",null)+
                "\n"+pref3.getString("check_username",null)+
                "\n"+pref3.getString("eula",null)+
                "\n"+pref3.getString("getLocation",null)+
                "\n"+pref3.getString("login",null)+
                "\n"+pref3.getString("register",null)+
                "\n"+pref3.getString("webpage",null)+
                "\n}");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("tncStatus",""+tncStatus);
                if(tncStatus!=null){
                    if(tncStatus.equals("true")) {
                        if (login_status != null) {
                            i = new Intent(SplashActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("last_activity", "SplashActivity.java");
                            i.putExtras(bundle);
                        } else {
                            i = new Intent(SplashActivity.this, LoginActivity.class);
                        }
                        startActivity(i);
                    }else{
                        TermsConditionsFragment termsConditionsFragment=new TermsConditionsFragment();
                        FragmentManager fragmentManager=getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.splashLayout,termsConditionsFragment).commit();
                    }
                }
                else{
                    TermsConditionsFragment termsConditionsFragment=new TermsConditionsFragment();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.splashLayout,termsConditionsFragment).commit();
                }
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
