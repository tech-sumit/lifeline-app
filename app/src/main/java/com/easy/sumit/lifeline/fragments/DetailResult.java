package com.easy.sumit.lifeline.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.volley.VolleyError;
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
    private Person person=new Person();

    private String user_name;
    private String name;
    private TextView name_text;
    private TextView gender_text;
    private TextView address_text;
    private TextView blood_group_text;
    private TextView last_donated_text;
    private Button previous_button;
    private Button next_button;
    private ContactHandler contactHandler;

    private static int view_count = 0;
    private int json_length = 0;
    private int previousState=0;

    public static DetailResult newInstance(JSONArray jsonarray,int viewCount,String user_name,String name) {
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
            args.putString(Constants.LAST_DONATED, jsonarray.getJSONObject(view_count).getString("last_donated"));
            fragment.setArguments(args);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.user_name=user_name;
        fragment.name =name;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getArguments().isEmpty()) {
            person.setUser_name(getArguments().getString(Constants.USER_NAME));
            person.setName(getArguments().getString(Constants.NAME));
            person.setBlood_group(getArguments().getString(Constants.BLOOD_GROUP));
            person.setGender(getArguments().getString(Constants.GENDER));
            person.setAddress(getArguments().getString(Constants.ADDRESS));
            person.setContact_no(getArguments().getString(Constants.CONTACT_NO));
            person.setLast_donated(getArguments().getString(Constants.LAST_DONATED));
            if(jsonArray!=null){
                json_length = jsonArray.length() - 1;
            }
        } else {
            Log.e("ERROR:", "Empty JSONArray, Bundle has null parameters.");
        }
        if(savedInstanceState!=null){
            if(!savedInstanceState.isEmpty()){
                person.setUser_name(savedInstanceState.getString(Constants.USER_NAME));
                person.setName(savedInstanceState.getString(Constants.NAME));
                person.setBlood_group(savedInstanceState.getString(Constants.BLOOD_GROUP));
                person.setGender(savedInstanceState.getString(Constants.GENDER));
                person.setAddress(savedInstanceState.getString(Constants.ADDRESS));
                person.setContact_no(savedInstanceState.getString(Constants.CONTACT_NO));
                person.setLast_donated(savedInstanceState.getString(Constants.LAST_DONATED));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_result, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name_text = (TextView) view.findViewById(R.id.nameText);
        gender_text = (TextView) view.findViewById(R.id.genderText);
        address_text = (TextView) view.findViewById(R.id.addressText);
        blood_group_text = (TextView) view.findViewById(R.id.bloodGroupText);
        last_donated_text = (TextView) view.findViewById(R.id.lastDonatedText);
        Button call_button = (Button) view.findViewById(R.id.call_button);
        previous_button = (Button) view.findViewById(R.id.previousButton);
        next_button = (Button) view.findViewById(R.id.nextButton);


        contactHandler = new ContactHandler(getActivity());
        name_text.setText(person.getName());
        gender_text.setText("Gender:".concat(person.getGender()));
        address_text.setText(person.getAddress());
        blood_group_text.setText(person.getBlood_group());
        last_donated_text.setText("Last Donated:".concat(person.getLast_donated()));
        if(view_count==0){
            previous_button.setEnabled(false);
            previous_button.setBackgroundColor(Color.argb(255,253,248,209));
        }

        if (view_count == json_length) {
            next_button.setEnabled(false);
            next_button.setBackgroundColor(Color.argb(255,253,248,209));
        }

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactHandler.setContact(person.getContact_no());
                //contactHandler.createContact();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + person.getContact_no()));
                startActivity(intent);
                Log.e("*****INFO*****","INSIDE Call button clicked");
                ((TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE))
                        .listen(new PhoneEndListener(),
                                PhoneStateListener.LISTEN_CALL_STATE);
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_count < json_length) {
                    view_count++;
                    try {
                        name_text.setText(jsonArray.getJSONObject(view_count).getString("name"));
                        gender_text.setText("Gender:".concat(jsonArray.getJSONObject(view_count).getString("gender")));
                        address_text.setText(jsonArray.getJSONObject(view_count).getString("address"));
                        blood_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                        last_donated_text.setText("Last Donated:".concat(jsonArray.getJSONObject(view_count).getString("last_donated")));
                        person.setContact_no(jsonArray.getJSONObject(view_count).getString("contact_no"));
                        person.setUser_name(jsonArray.getJSONObject(view_count).getString("user_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    previous_button.setEnabled(true);
                    previous_button.setBackgroundColor(Color.argb(255,255,249,196));
                }
                if (view_count == (json_length)) {
                    next_button.setEnabled(false);
                    next_button.setBackgroundColor(Color.argb(255,253,248,209));
                }
            }
        });
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_count > 0) {
                    view_count--;
                    try {
                        name_text.setText(jsonArray.getJSONObject(view_count).getString("name"));
                        gender_text.setText("Gender:".concat(jsonArray.getJSONObject(view_count).getString("gender")));
                        address_text.setText(jsonArray.getJSONObject(view_count).getString("address"));
                        blood_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                        last_donated_text.setText("Last Donated:".concat(jsonArray.getJSONObject(view_count).getString("last_donated")));
                        person.setContact_no(jsonArray.getJSONObject(view_count).getString("contact_no"));
                        person.setUser_name(jsonArray.getJSONObject(view_count).getString("user_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    next_button.setEnabled(true);
                    next_button.setBackgroundColor(Color.argb(255,255,249,196));
                }
                if (view_count == 0) {
                    previous_button.setEnabled(false);
                    previous_button.setBackgroundColor(Color.argb(255,253,248,209));
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_NAME,person.getUser_name());
        bundle.putString(Constants.NAME,person.getName());
        bundle.putString(Constants.BLOOD_GROUP,person.getBlood_group());
        bundle.putString(Constants.GENDER,person.getGender());
        bundle.putString(Constants.ADDRESS,person.getAddress());
        bundle.putString(Constants.CONTACT_NO,person.getContact_no());
        bundle.putString(Constants.LAST_DONATED,person.getLast_donated());
        outState.putAll(bundle);
    }

    private class PhoneEndListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged ( int state, String incomingNumber){
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "http://10.0.2.2:9090/lifeline_app/call_log.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("***INFO***", "Review Added:" + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> stringMap = new HashMap<>();
                    stringMap.put("user_name", ""+person.getUser_name());
                    stringMap.put("choice", "1");
                    stringMap.put("from_user", ""+name);
                    stringMap.put("from_user_name", ""+user_name);
                    Log.i("Data call_log","user_name: "+user_name+"name: "+name+"\nfrom_user: "+person.getUser_name());
                    return stringMap;
                }
            };
            try{
                switch (state) {
                    //Hangup
                    case TelephonyManager.CALL_STATE_IDLE:
                        if(previousState==TelephonyManager.CALL_STATE_OFFHOOK){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    contactHandler.deleteLog();
                                    if(getActivity()!=null){
                                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                        requestQueue.add(stringRequest);
                                    }else{
                                        Log.e("ERROR","Null Context while passing it to request queue adding call log");
                                    }
                                }
                            }, 1000);

                        }
                        previousState=TelephonyManager.CALL_STATE_IDLE;
                        Log.i("Call Status", "CALL_STATE_IDLE");
                        break;
                    //Outgoing
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //if(previousState==TelephonyManager.CALL_STATE_IDLE){}
                        previousState=TelephonyManager.CALL_STATE_OFFHOOK;
                        Log.i("Call Status", "CALL_STATE_OFFHOOK");
                        break;
                    //Incoming
                    case TelephonyManager.CALL_STATE_RINGING:
                        previousState=TelephonyManager.CALL_STATE_RINGING;
                        Log.i("Call Status", "CALL_STATE_RINGING");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}