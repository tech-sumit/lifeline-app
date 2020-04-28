package com.easy.sumit.lifeline.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.RemoteDataRetriever;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements AsyncResponse{

    private TextView personName,personMail,personAddress,personContact,personBloodGroup;
    private String user_name="";

    private RemoteDataRetriever remoteDataRetriver;

    public ProfileFragment() {

    }

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

        user_name=getArguments().getString("user_name");
        remoteDataRetriver=new RemoteDataRetriever(this);
        remoteDataRetriver.execute(user_name,"1","0");
    }

    @Override
    public void processFinish(String output) {
        try {
            Log.i("******OUTPUT******",output);
            JSONObject json=new JSONObject(output);
            personName.setText(json.getString("name"));
            personMail.setText(json.getString("user_mail"));
            personAddress.setText(json.getString("address"));
            personContact.setText("Contact No. "+json.getString("contact_no"));
            personBloodGroup.setText(json.getString("blood_group"));
            remoteDataRetriver.cancel(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
