package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.content.Intent;
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
import com.easy.sumit.lifeline.activities.RegisterActivity;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;

import java.util.HashMap;
import java.util.Map;

public class RegisterBackgroundWorker{

    private Person person;
    private RegisterActivity registerActivity;

    public RegisterBackgroundWorker(RegisterActivity registerActivity) {
        this.registerActivity=registerActivity;
    }
    public void updateData(Person person){
        this.person=person;
    }
    public void start(){
        String url = "http://10.0.2.2:9090/lifeline_app/register.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Responce",""+response);
                registerActivity.progressDialog.dismiss();
                Toast.makeText(registerActivity, response, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(registerActivity, LoginActivity.class);
                registerActivity.startActivity(intent);
                registerActivity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registerActivity.progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(registerActivity, "Connection Failed", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("INFO","Username: "+person.getUser_name()+
                        "\nMail: "+person.getUser_mail()+
                        "\nPassword: "+person.getUser_pass()+
                        "\nName: "+person.getName()+
                        "\nAge: "+person.getAge()+
                        "\nBlood group: "+person.getBlood_group()+
                        "\nGender: "+person.getGender()+
                        "\nAddress: "+person.getAddress()+
                        "\nContact no.: "+person.getContact_no()+
                        "\nState: "+person.getState()+
                        "\nDistrict: "+person.getDistrict()+
                        "\nSub district: "+person.getSub_district()+
                        "\nLast donated: "+person.getLast_donated());

                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name",person.getUser_name());
                stringMap.put("user_mail",person.getUser_mail());
                stringMap.put("user_pass",person.getUser_pass());
                stringMap.put("name",person.getName());
                stringMap.put("blood_group",person.getBlood_group());
                stringMap.put("gender",person.getGender());
                stringMap.put("age",person.getAge());
                stringMap.put("last_donated",person.getLast_donated());
                stringMap.put("address",person.getAddress());
                stringMap.put("contact_no",person.getContact_no());
                stringMap.put("state",person.getState());
                stringMap.put("district",person.getDistrict());
                stringMap.put("sub_district",person.getSub_district());

                return stringMap;

            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(registerActivity);
        requestQueue.add(stringRequest);
    }
}
