package com.easy.sumit.lifeline.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class NewsService extends Service {
    long cacheExpiration = 7200;
    FirebaseRemoteConfigSettings remoteConfigSettings;
    public NewsService() {}

    @Override
    public void onCreate() {
        super.onCreate();
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
                Log.i("Config","On the go!!!");
                SharedPreferences pref = getApplicationContext().getSharedPreferences("lifeline_news", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
/*
                editor.putString("call_log",jsonObject.getString("call_log"));
                editor.putString("getData",jsonObject.getString("getData"));
                editor.putString("check_username",jsonObject.getString("check_username"));
                editor.putString("eula",jsonObject.getString("eula"));
                editor.putString("getLocation",jsonObject.getString("getLocation"));
                editor.putString("login",jsonObject.getString("login"));
                editor.putString("register",jsonObject.getString("register"));
                editor.putString("webpage",jsonObject.getString("webpage"));
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
           */ }

        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startService(new Intent(getApplicationContext(), NewsService.class));
    }
}
