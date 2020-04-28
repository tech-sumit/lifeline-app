package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteLocationRetriever{

    private Activity activity;
    private Spinner spinner;
    private Bundle bundle;

    public RemoteLocationRetriever(Activity activity) {
        this.activity=activity;
    }
    public void updateData(Spinner spinner,Bundle bundle){
        this.spinner=spinner;
        this.bundle=bundle;
    }
    public void start(){
        String url = "http://10.0.2.2:9090/lifeline_app/getLocation.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                (Response.Listener<String>) response -> initSpinner(spinner,response),
                (Response.ErrorListener) Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();

                stringMap.put("user_name",bundle.getString(Constants.USER_NAME));
                stringMap.put("db_action",bundle.getString("db_action"));
                stringMap.put("location_level",bundle.getString("location_level"));
                stringMap.put("data",bundle.getString("data"));
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    private void initSpinner(Spinner spinner, String string){
        try {
            String label="";
            switch (bundle.getString("location_level")) {
                case "1":
                    label = "state";
                    break;
                case "2":
                    label = "district";
                    break;
                case "3":
                    label = "sub_district";
                    break;
            }
            Log.i("Locaion Level:",""+label);
            Log.i("JSON Input String:",""+string);
            ArrayList<String> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(string);
            for(int i = 0; i< jsonArray.length(); i++){
                Log.d("\n"+label+" "+i, jsonArray.getString(i));
                arrayList.add(jsonArray.getJSONObject(i).getString(label));
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_dropdown_item_1line,
                    arrayList);
            spinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}