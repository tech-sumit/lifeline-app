package com.easy.sumit.lifeline.backgroundworkers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.datamodal.URLList;
import com.easy.sumit.lifeline.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Volley.newRequestQueue(activity).add(
                new StringRequest(Request.Method.POST,
                        URLList.getUrl(activity,"getLocation"),
                        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                initSpinner(spinner, response);
            }
        },
                        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(activity, "Connection Failed", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();

                stringMap.put("user_name",bundle.getString(Constants.USER_NAME));
                stringMap.put("db_action",bundle.getString("db_action"));
                stringMap.put("location_level",bundle.getString("location_level"));
                stringMap.put("data",bundle.getString("data"));
                Log.i("Data tobe posted",""+bundle.getString(Constants.USER_NAME)
                        +"\n"+bundle.getString("db_action")
                        +"\n"+bundle.getString("location_level")
                        +"\n"+bundle.getString("data"));
                return stringMap;
            }
        });
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
            JSONObject arrayList_ID = new JSONObject();
            for(int i = 0; i< jsonArray.length(); i++){
                Log.d("\n"+label+" "+i, jsonArray.getString(i));
                arrayList.add(jsonArray.getJSONObject(i).getString(label));
                arrayList_ID.put(""+i,""+jsonArray.getJSONObject(i).getString(label+"_id"));
            }
            activity.getSharedPreferences("lifeline_data", Context.MODE_PRIVATE).edit().putString(""+label,""+arrayList_ID.toString()).apply();
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_dropdown_item_1line,
                    arrayList);
            spinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
