package com.easy.sumit.lifeline.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment{

    private TextView personName,personMail,personAddress,personContact,personBloodGroup;
    private String user_name="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personName=(TextView) getView().findViewById(R.id.personName);
        personMail= (TextView) getView().findViewById(R.id.personEmail);
        personAddress= (TextView) getView().findViewById(R.id.personAddress);
        personContact= (TextView) getView().findViewById(R.id.personContact);
        personBloodGroup= (TextView) getView().findViewById(R.id.personBloodGroup);

        user_name=getArguments().getString(Constants.USER_NAME);
        String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, (Response.Listener<String>) response -> {
            try {
                JSONObject json=new JSONObject(response);
                personName.setText(json.getString("name"));
                personMail.setText(json.getString("user_mail"));
                personAddress.setText(json.getString("address"));
                personContact.setText("Contact No. "+json.getString("contact_no"));
                personBloodGroup.setText(json.getString("blood_group"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (Response.ErrorListener) Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                try {
                    stringMap.put(URLEncoder.encode("user_name", "UTF-8"),
                            URLEncoder.encode(user_name, "UTF-8"));
                    stringMap.put(URLEncoder.encode("db_action", "UTF-8"),
                            URLEncoder.encode("1", "UTF-8"));
                    stringMap.put(URLEncoder.encode("total_data", "UTF-8"),
                            URLEncoder.encode("0", "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this.getActivity());
        requestQueue.add(stringRequest);
    }
}
