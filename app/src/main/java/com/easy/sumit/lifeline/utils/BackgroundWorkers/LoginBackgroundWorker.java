package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.Activities.LoginActivity;
import com.easy.sumit.lifeline.Activities.MainActivity;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginBackgroundWorker{
    private LoginActivity loginActivity;
    String login_url = "http://10.0.2.2:9090/lifeline_app/login.php";
    private boolean login_status;
    private Person person;
    public LoginBackgroundWorker(LoginActivity loginActivity, Person person){
        this.loginActivity=loginActivity;
        this.person=person;
    }
    public void start(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, login_url, (Response.Listener<String>) response -> {
            loginActivity.progressDialog.dismiss();
            Toast.makeText(loginActivity, response, Toast.LENGTH_LONG).show();
            if (!response.equals("")) {
                login_status = checkLogin(response);
                if (login_status) {
                    Intent intent = new Intent(loginActivity, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.USER_NAME, person.getUser_name());
                    bundle.putString(Constants.NAME, person.getName());
                    bundle.putString("last_activity", "LoginActivity.java");
                    intent.putExtras(bundle);
                    SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("lifeline",
                            loginActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("login_status", "true");

                    loginActivity.startActivity(intent);
                    Log.i("LOGIN", "Login Success");
                } else {
                    Log.i("LOGIN", "Login Failed");
                }
            } else {
                Log.e("Output Exception", "Output String is null : " + response);
            }
        }, (Response.ErrorListener) error -> {
            error.printStackTrace();
            loginActivity.progressDialog.dismiss();
            Toast.makeText(loginActivity, "Connection Failed", Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String > stringMap=new HashMap<>();
                try {
                    String IMEI_NO=getIMEI();
                    stringMap.put(URLEncoder.encode("user_name", "UTF-8"),
                            URLEncoder.encode(person.getUser_name(),"UTF-8"));
                    stringMap.put(URLEncoder.encode("user_pass", "UTF-8"),
                            URLEncoder.encode(person.getUser_pass(),"UTF-8"));
                    stringMap.put(URLEncoder.encode("IMEI_NO", "UTF-8"),
                            URLEncoder.encode(IMEI_NO,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(loginActivity);
        requestQueue.add(stringRequest);

    }

    private String getIMEI(){
        if (ActivityCompat.checkSelfPermission(loginActivity
                , Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(loginActivity,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        TelephonyManager telephonyManager= (TelephonyManager)loginActivity.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    private boolean checkLogin(String output){
        try{
            StringBuilder outputBuilder=new StringBuilder(output);
            if(outputBuilder.substring(0,13).equals("Login success")){
                person.setName(outputBuilder.substring(22));
                login_status=true;
            }
            else
            {
                login_status=false;
            }

        }catch(StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return login_status;
    }
}