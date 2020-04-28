package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.activities.LoginActivity;
import com.easy.sumit.lifeline.activities.MainActivity;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.Constants;

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
        StringRequest stringRequest=new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.LOGIN_STATUS, "true");
                        editor.apply();
                        loginActivity.startActivity(intent);
                        loginActivity.finish();
                        Log.i("LOGIN", "Login Success" + response);
                    } else {
                        Log.i("LOGIN", "Login Failed" + response);
                    }
                } else {
                    Log.e("Output Exception", "Output String is null : " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loginActivity.progressDialog.dismiss();
                Toast.makeText(loginActivity, "Connection Failed", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String > stringMap=new HashMap<>();
                stringMap.put("user_name",""+person.getUser_name());
                stringMap.put("user_pass",""+person.getUser_pass());
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(loginActivity);
        requestQueue.add(stringRequest);

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