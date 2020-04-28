package com.easy.sumit.lifeline.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.ContactHandler;

import org.json.JSONArray;
import org.json.JSONException;

public class DetailResult extends Fragment {

    private static JSONArray jsonArray;
    private String name;
    private String blood_group;
    private String gender;
    private String address;
    private String contact_no;
    private String hiv_status;

    private TextView name_text;
    private TextView gender_text;
    private TextView address_text;
    private TextView bloog_group_text;
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
            args.putString("name", jsonarray.getJSONObject(view_count).getString("name"));
            args.putString("blood_group", jsonarray.getJSONObject(view_count).getString("blood_group"));
            args.putString("gender", jsonarray.getJSONObject(view_count).getString("gender"));
            args.putString("address", jsonarray.getJSONObject(view_count).getString("address"));
            args.putString("contact_no", jsonarray.getJSONObject(view_count).getString("contact_no"));
            args.putString("hiv_status", jsonarray.getJSONObject(view_count).getString("hiv_status"));
            fragment.setArguments(args);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public DetailResult() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            blood_group = getArguments().getString("blood_group");
            gender = getArguments().getString("gender");
            address = getArguments().getString("address");
            contact_no = getArguments().getString("contact_no");
            hiv_status = getArguments().getString("hiv_status");
            json_length = jsonArray.length() - 1;
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
        bloog_group_text = (TextView) getActivity().findViewById(R.id.bloodGroupText);
        hiv_status_text = (TextView) getActivity().findViewById(R.id.hivStatusText);
        Button call_button = (Button) getActivity().findViewById(R.id.call_button);
        previous_button = (Button) getActivity().findViewById(R.id.previousButton);
        next_button = (Button) getActivity().findViewById(R.id.nextButton);


        contactHandler = new ContactHandler(getContext());
        Log.i("Name:", name);
        Log.i("Gender:", gender);
        Log.i("Address:", address);
        Log.i("Blood Group:", blood_group);
        Log.i("HIV Status:", hiv_status);

        name_text.setText(name);
        gender_text.setText("Gender:" + gender);
        address_text.setText(address);
        bloog_group_text.setText(blood_group);
        hiv_status_text.setText("HIV Status: " + hiv_status);
        Log.i("json_length", "" + json_length);
        if(view_count==0){
            previous_button.setEnabled(false);
        }
        if (view_count == json_length) {
            next_button.setEnabled(false);
        }
        call_button.setOnClickListener(view1 -> {
            contactHandler.setContact(contact_no);
            Log.i("contact_no",contact_no);
            contactHandler.createContact();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+contact_no));
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
                    bloog_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                    hiv_status_text.setText("HIV Status:" + jsonArray.getJSONObject(view_count).getString("hiv_status"));
                    contact_no = jsonArray.getJSONObject(view_count).getString("contact_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                previous_button.setEnabled(true);
                Log.i("view_count", "" + view_count);
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
                    bloog_group_text.setText(jsonArray.getJSONObject(view_count).getString("blood_group"));
                    hiv_status_text.setText("HIV Status:" + jsonArray.getJSONObject(view_count).getString("hiv_status"));
                    contact_no = jsonArray.getJSONObject(view_count).getString("contact_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                next_button.setEnabled(true);
                Log.i("view_count", "" + view_count);
            }
            if (view_count == 0) {
                previous_button.setEnabled(false);
            }
        });
    }
    private class PhoneEndListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                //Hangup
                case TelephonyManager.CALL_STATE_IDLE:

                    if(IDL_COUNT==1){
                        contactHandler.deleteContact();
                        //contactHandler.deleteLog();
                        IDL_COUNT=0;
                    }
                    IDL_COUNT++;
                    Log.i("Call Status","CALL_STATE_IDLE");
                    break;
                //Outgoing
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("Call Status","CALL_STATE_OFFHOOK");
                    break;
                //Incoming
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("Call Status","CALL_STATE_RINGING");
                    break;
            }
        }
    }
}
