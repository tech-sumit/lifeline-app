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
    Person person = new Person();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView personName = (TextView) getView().findViewById(R.id.personNameView);
        TextView personUserName = (TextView) getView().findViewById(R.id.personUserNameView);
        TextView personMail = (TextView) getView().findViewById(R.id.personEmailView);
        TextView personAddress = (TextView) getView().findViewById(R.id.personAddressView);
        TextView personContact = (TextView) getView().findViewById(R.id.personContactView);
        TextView personLastDonated = (TextView) getView().findViewById(R.id.personLastDonatedView);
        TextView personBloodGroup = (TextView) getView().findViewById(R.id.personBloodGroupView);
        TextView personGender= (TextView) getView().findViewById(R.id.personGenderView);
        TextView personAge= (TextView) getView().findViewById(R.id.personAgeView);
        TextView personState= (TextView) getView().findViewById(R.id.personStateView);
        TextView personDistrict= (TextView) getView().findViewById(R.id.personDistrictView);
        TextView personSubdistrict= (TextView) getView().findViewById(R.id.personSubdistrictView);

    /*
    private String user_pass=null;
    */
        person.setAllByPreferences(getContext());

        personName.setText(person.getName());
        personAge.setText("Age: "+person.getAge());
        personBloodGroup.setText("Blood Group: "+person.getBlood_group());
        personGender.setText("Gender: "+person.getGender());
        personLastDonated.setText("Last Donated: "+person.getLast_donated());
        personMail.setText("Mail: "+person.getUser_mail());
        personContact.setText("Contact No. ".concat(person.getContact_no()));
        personAddress.setText("Address: "+person.getAddress());
        personUserName.setText("Username: "+person.getUser_name());
        personState.setText("State: "+person.getState());
        personDistrict.setText("District: "+person.getDistrict());
        personSubdistrict.setText("Sub district: "+person.getSub_district());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(person!=null){
            person.setAllByPreferences(getContext());
        }
    }
}
