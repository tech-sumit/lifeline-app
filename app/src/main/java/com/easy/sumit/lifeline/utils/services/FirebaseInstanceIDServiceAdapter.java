package com.easy.sumit.lifeline.utils.services;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInstanceIDServiceAdapter extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.i("Firebase ID:",FirebaseInstanceId.getInstance().getId());
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://10.0.2.2:9090/lifeline_app/fcmnotify.php",
                (Response.Listener<String>) response -> Log.i("Token Update Responce","responce:"+response),
                (Response.ErrorListener) Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name","");
                stringMap.put("firebaseID",token);
                stringMap.put("choice","1");
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
