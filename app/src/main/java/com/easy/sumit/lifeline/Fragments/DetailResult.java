package com.easy.sumit.lifeline.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.Constants;
import com.easy.sumit.lifeline.utils.ContactHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class DetailResult extends Fragment {

    private static JSONArray jsonArray;
    private Person person;

    private TextView name_text;
    private TextView gender_text;
    private TextView address_text;
    private TextView blood_group_text;
    private TextView hiv_status_text;
    private Button previous_button;
    private Button next_button;
    private ContactHandler contactHandler;

    private static int view_count = 0;
    private int json_length = 0;
    private int IDL_COUNT=0;

    public static DetailResult newInstance(JSONArray jsonarray,int viewCount) {
        jsonArray = jsonarray;
        view_count=viewCount;
        DetailResult fragment = new DetailResult();
        Bundle args = new Bundle();
        try {
            args.putString(Constants.USER_NAME, jsonarray.getJSONObject(view_count).getString("user_name"));
            args.putString(Constants.NAME, jsonarray.getJSONObject(view_count).getString("name"));
            args.putString(Constants.BLOOD_GROUP, jsonarray.getJSONObject(view_count).getString("blood_group"));
            args.putString(Constants.GENDER, jsonarray.getJSONObject(view_count).getString("gender"));
            args.putString(Constants.ADDRESS, jsonarray.getJSONObject(view_count).getString("address"));
            args.putString(Constants.CONTACT_NO, jsonarray.getJSONObject(view_count).getString("contact_no"));
            args.putString(Constants.HIV_STATUS, jsonarray.getJSONObject(view_count).getString("hiv_status"));
            fragment.setArguments(args);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person=new Person();
            person.setUser_name(getArguments().getString(Constants.USER_NAME));
            person.setName(getArguments().getString(Constants.NAME));
            person.setBlood_group(getArguments().getString(Constants.BLOOD_GROUP));
            person.setGender(getArguments().getString(Constants.GENDER));
            person.setAddress(getArguments().getString(Constants.ADDRESS));
            person.setContact_no(getArguments().getString(Constants.CONTACT_NO));
            person.setHiv_status(getArguments().getString(Constants.HIV_STATUS));
            if(jsonArray!=null){
                json_length = jsonArray.length() - 1;
            }
        } else {
            Log.e("ERROR:", "Empty JSONArray, Bundle has null parameters.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name_text = (TextView) getActivity().findViewById(R.id.nameText);
        gender_text = (TextView) getActivity().findViewById(R.id.genderText);
        address_text = (TextView) getActivity().findViewById(R.id.addressText);
        blood_group_text = (TextView) getActivity().findViewById(R.id.bloodGroupText);
        hiv_status_text = (TextView) getActivity().findViewById(R.id.hivStatusText);
        Button call_button = (Button) getActivity().findViewById(R.id.call_button);
        previous_button = (Button) getActivity().findViewById(R.id.previousButton);
        next_button = (Button) getActivity().findViewById(R.id.nextButton);


        contactHandler = new ContactHandler(getActivity());
        name_text.setText(person.getName());
        gender_text.setText("Gender:" + person.getGender());
        address_text.setText(person.getAddress());
        blood_group_text.setText(person.getBlood_group());
        hiv_status_text.setText("HIV Status: " + person.getHiv_status());
        if(view_count==0){
            previous_button.setEnabled(false);
        }
        if (view_count == json_length) {
            next_button.setEnabled(false);
        }
        call_button.setOnClickListener(view1 -> {
            contactHandler.setContact(person.getContact_no());
            //contactHandler.createContact();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+person.getContact_no()));
            startActivity(intent);
            ((TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE))
                    .listen(new PhoneEndListener(),
                    PhoneStateListener.LISTEN_CALL_STATE);
        });
        next_button.setOnClickListener(view1 -> {
            if (view_count < json_length) {
                view_count++;
                try {
                    name_text.setText(jsonArray.getJSONObject(view_count).getString("name"));
                    gender_text.setText("Gender:" + jsonArray.getJSONObject(view_count).getString("gender"));
                    address_text.setText(jsonArray.getJSONObject(view_count).getString("address"));
                    blood_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                    hiv_status_text.setText("HIV Status:" + jsonArray.getJSONObject(view_count).getString("hiv_status"));
                    person.setContact_no(jsonArray.getJSONObject(view_count).getString("contact_no"));
                    person.setUser_name(jsonArray.getJSONObject(view_count).getString("user_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                previous_button.setEnabled(true);
            }
            if (view_count == (json_length)) {
                next_button.setEnabled(false);
            }
        });
        previous_button.setOnClickListener(view1 -> {
            if (view_count > 0) {
                view_count--;
                try {
                    name_text.setText(jsonArray.getJSONObject(view_count).getString("name"));
                    gender_text.setText("Gender:" + jsonArray.getJSONObject(view_count).getString("gender"));
                    address_text.setText(jsonArray.getJSONObject(view_count).getString("address"));
                    blood_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                    hiv_status_text.setText("HIV Status:" + jsonArray.getJSONObject(view_count).getString("hiv_status"));
                    person.setContact_no(jsonArray.getJSONObject(view_count).getString("contact_no"));
                    person.setUser_name(jsonArray.getJSONObject(view_count).getString("user_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                next_button.setEnabled(true);
            }
            if (view_count == 0) {
                previous_button.setEnabled(false);
            }
        });
    }
    private class PhoneEndListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged ( int state, String incomingNumber){
            try{
                switch (state) {
                    //Hangup
                    case TelephonyManager.CALL_STATE_IDLE:

                        if (IDL_COUNT == 1) {
                            //contactHandler.deleteContact();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                    "http://10.0.2.2:9090/lifeline_app/fcmnotify.php",
                                    (Response.Listener<String>) response ->
                                            Log.i("***INFO***", "Notification Sent:" + response),
                                    (Response.ErrorListener) error -> {
                                    error.printStackTrace();
                                    Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> stringMap = new HashMap<>();
                                    stringMap.put("user_name", person.getUser_name());
                                    stringMap.put("choice", "2");
                                    return stringMap;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.add(stringRequest);
                            IDL_COUNT = 0;
                            new Handler().postDelayed(() -> contactHandler.deleteLog(),1000);
                        }
                        IDL_COUNT++;
                        Log.i("Call Status", "CALL_STATE_IDLE");
                        break;
                    //Outgoing
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.i("Call Status", "CALL_STATE_OFFHOOK");
                        break;
                    //Incoming
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.i("Call Status", "CALL_STATE_RINGING");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}