package com.easy.sumit.lifeline.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.activities.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class UrlUpdateService extends Service {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    long cacheExpiration = 7200;
    FirebaseRemoteConfigSettings remoteConfigSettings;
    public UrlUpdateService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.url_config);
        Log.i("UrlUpdateService","onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("UrlUpdateService","onStart()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                    cacheExpiration = 0;
                }
                Log.i("Config","On the go!!!");
                mFirebaseRemoteConfig.fetch(60)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mFirebaseRemoteConfig.activateFetched();

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("lifeline_urls", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("call_log",mFirebaseRemoteConfig.getString("call_log"));
                                    editor.putString("getData",mFirebaseRemoteConfig.getString("getData"));
                                    editor.putString("check_username",mFirebaseRemoteConfig.getString("check_username"));
                                    editor.putString("eula",mFirebaseRemoteConfig.getString("eula"));
                                    editor.putString("getLocation",mFirebaseRemoteConfig.getString("getLocation"));
                                    editor.putString("login",mFirebaseRemoteConfig.getString("login"));
                                    editor.putString("register",mFirebaseRemoteConfig.getString("register"));
                                    editor.putString("webpage",mFirebaseRemoteConfig.getString("webpage"));
                                    editor.putString("flag","true");
                                    editor.apply();
                                    Log.i("***Data Update Pref.***","Urls{"+
                                            "\n"+pref.getString("call_log",null)+
                                            "\n"+pref.getString("getData",null)+
                                            "\n"+pref.getString("check_username",null)+
                                            "\n"+pref.getString("eula",null)+
                                            "\n"+pref.getString("getLocation",null)+
                                            "\n"+pref.getString("login",null)+
                                            "\n"+pref.getString("register",null)+
                                            "\n"+pref.getString("webpage",null)+
                                            "\n}");
                                }
                                Toast.makeText(getApplicationContext(), "URLs Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startService(new Intent(getApplicationContext(), UrlUpdateService.class));
    }
}
