package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.Activities.LoginActivity;
import com.easy.sumit.lifeline.Activities.RegisterActivity;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.google.firebase.iid.FirebaseInstanceId;

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
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, (Response.Listener<String>) response -> {
            registerActivity.progressDialog.dismiss();
            Toast.makeText(registerActivity,response,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(registerActivity, LoginActivity.class);
            registerActivity.startActivity(intent);

        },(Response.ErrorListener) error -> {
                    error.printStackTrace();
                    Toast.makeText(registerActivity, "Connection Failed", Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name",person.getUser_name());
                stringMap.put("user_mail",person.getUser_mail());
                stringMap.put("user_pass",person.getUser_pass());
                stringMap.put("name",person.getName());
                stringMap.put("blood_group",person.getBlood_group());
                stringMap.put("gender",person.getGender());
                stringMap.put("age",person.getAge());
                stringMap.put("hiv_status",person.getHiv_status());
                stringMap.put("address",person.getAddress());
                stringMap.put("contact_no",person.getContact_no());
                stringMap.put("state",person.getState());
                stringMap.put("district",person.getDistrict());
                stringMap.put("sub_district",person.getSub_district());
                stringMap.put("IMEI_NO",person.getImei_no());

                String fID = FirebaseInstanceId.getInstance().getToken();
                stringMap.put("firebaseID",fID);
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(registerActivity.getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
