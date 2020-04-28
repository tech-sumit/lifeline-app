package com.easy.sumit.lifeline.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easy.sumit.lifeline.datamodal.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsService extends Service {
    long newsRefreshRate = 7200;
    Thread thread=null;
    public NewsService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("UrlUpdateService","onCreate()");
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while(true){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Config","On the go!!!");
                            final SharedPreferences pref = getApplicationContext().getSharedPreferences("lifeline_news", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = pref.edit();
                            StringRequest stringRequest =new StringRequest(Request.Method.POST,
                                    /*URL*/"",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject= null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                editor.putString("lifeline_news_data",jsonObject.getString("lifeline_news_data"));
                                                editor.apply();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Person person=new Person();
                                    person.setAllByPreferences(getApplicationContext());
                                    Map<String ,String > stringMap=new HashMap<>();
                                    stringMap.put("user_name",""+person.getUser_name());
                                    stringMap.put("db_action","14");
                                    return stringMap;
                                }
                            };
                        }
                    },newsRefreshRate);
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("UrlUpdateService","onStart()");
        if(thread!=null){
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if(thread!=null){
            thread.stop();
        }
    }
}
