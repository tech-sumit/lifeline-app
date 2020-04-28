package com.easy.sumit.lifeline.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;

public class ProfileFragment extends Fragment{

    private Person person;
    private TextView personName,personMail,personAddress,personContact,personBloodGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personName=(TextView) getView().findViewById(R.id.personNameView);
        personMail= (TextView) getView().findViewById(R.id.personEmailView);
        personAddress= (TextView) getView().findViewById(R.id.personAddressView);
        personContact= (TextView) getView().findViewById(R.id.personContactView);
        personBloodGroup= (TextView) getView().findViewById(R.id.personBloodGroupView);
        person=new Person();
        person.setAllByPreferences(getContext());

        personName.setText(person.getName());
        personMail.setText(person.getUser_mail());
        personAddress.setText(person.getAddress());
        personContact.setText("Contact No. "+person.getContact_no());
        personBloodGroup.setText(person.getBlood_group());
    }
}
