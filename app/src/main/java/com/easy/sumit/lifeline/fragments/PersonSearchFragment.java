package com.easy.sumit.lifeline.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RemoteDataRetriever;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RemoteLocationRetriever;
import com.easy.sumit.lifeline.utils.Constants;

public class PersonSearchFragment extends Fragment implements View.OnClickListener{

    public Spinner personBloodGroup;
    private Spinner stateSpinner;
    private Spinner districtSpinner;
    private Spinner sub_districtSpinner;
    public Button buttonSearch;
    private String bloodGroup="";
    private String state="";
    private String district="";
    private String sub_district="";
    private int d=0;
    private int s=0;
    private int u=0;
    public ProgressDialog progressDialog;
    private Person person;
    private RemoteLocationRetriever remoteLocationRetriever;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person=new Person();
        person.setAllByPreferences(getContext());
        if(savedInstanceState!=null){
            if(!savedInstanceState.isEmpty()){
                personBloodGroup.setSelection(Integer.parseInt(savedInstanceState.getString(Constants.BLOOD_GROUP)));
                stateSpinner.setSelection(Integer.parseInt(savedInstanceState.getString(Constants.STATE)));
                districtSpinner.setSelection(Integer.parseInt(savedInstanceState.getString(Constants.DISTRICT)));
                sub_districtSpinner.setSelection(Integer.parseInt(savedInstanceState.getString(Constants.SUB_DISTRICT)));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSearch= (Button) getActivity().findViewById(R.id.buttonSearch);

        personBloodGroup= (Spinner) getActivity().findViewById(R.id.spinnerBloodGroup);
        stateSpinner= (Spinner) getActivity().findViewById(R.id.stateSelector);
        districtSpinner= (Spinner) getActivity().findViewById(R.id.districtSelector);
        sub_districtSpinner= (Spinner) getActivity().findViewById(R.id.sub_districtSelector);
        remoteLocationRetriever=new RemoteLocationRetriever(getActivity());
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.state,
                android.R.layout.simple_dropdown_item_1line);
        stateSpinner.setAdapter(arrayAdapter);

        stateSpinner.setOnItemSelectedListener(new StateEventListener(this));
        districtSpinner.setOnItemSelectedListener(new DistrictEventListener(this));
        sub_districtSpinner.setOnItemSelectedListener(new SubDistrictEventListener(this));
        ArrayAdapter<CharSequence> arrayAdapterBloodGroup = ArrayAdapter.
                createFromResource(getContext(),
                        R.array.blood_groups,
                        R.layout.support_simple_spinner_dropdown_item);
        personBloodGroup.setAdapter(arrayAdapterBloodGroup);
        personBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = personBloodGroup.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        RemoteDataRetriever remoteDataRetriever = new RemoteDataRetriever(this);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_NAME,person.getUser_name());
        bundle.putString("db_action","2");
        bundle.putString("total_data","4");
        bundle.putString("data"+1,bloodGroup);
        bundle.putString("data"+2,state);
        bundle.putString("data"+3,district);
        bundle.putString("data"+4,sub_district);
        progressDialog=new ProgressDialog(this.getContext());
        progressDialog.setMessage("Searching...");
        progressDialog.show();
        if(!bloodGroup.equals("")&&!isBanned()) {
            remoteDataRetriever.updateData(bundle);
            remoteDataRetriever.start();
        }
        else{
            Snackbar.make(view,
                          "Please select blood group",
                          Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    private boolean isBanned(){
        SharedPreferences pref = getActivity().getSharedPreferences("lifeline", Context.MODE_PRIVATE);
        boolean status=false;
        String account_status=pref.getString("account_status",null);
        if(account_status!=null){
            if(account_status.equals("banned")){
                status=true;
            }
        }
        return status;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.BLOOD_GROUP, String.valueOf(personBloodGroup.getSelectedItemPosition()));
        bundle.putString(Constants.STATE, String.valueOf(stateSpinner.getSelectedItemPosition()));
        bundle.putString(Constants.DISTRICT, String.valueOf(districtSpinner.getSelectedItemPosition()));
        bundle.putString(Constants.SUB_DISTRICT, String.valueOf(sub_districtSpinner.getSelectedItemPosition()));
        outState.putAll(bundle);
    }

    private class StateEventListener implements AdapterView.OnItemSelectedListener{
        PersonSearchFragment personSearchFragment;
        StateEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            state=stateSpinner.getSelectedItem().toString();
            Log.d("***INFO***","State:"+state);
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,person.getUser_name());
            bundle.putString("db_action","1");
            bundle.putString("location_level","2");
            bundle.putString("data",state);
            remoteLocationRetriever.updateData(districtSpinner,bundle);
            remoteLocationRetriever.start();
            s=1;
            d=0;
            u=0;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    private class DistrictEventListener implements AdapterView.OnItemSelectedListener {
        PersonSearchFragment personSearchFragment;
        DistrictEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            district=districtSpinner.getSelectedItem().toString();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,person.getUser_name());
            bundle.putString("db_action","1");
            bundle.putString("location_level","3");
            bundle.putString("data",district);
            remoteLocationRetriever.updateData(sub_districtSpinner,bundle);
            remoteLocationRetriever.start();

            d=1;
            u=0;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class SubDistrictEventListener implements AdapterView.OnItemSelectedListener {
        PersonSearchFragment personSearchFragment;
        SubDistrictEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sub_district=sub_districtSpinner.getSelectedItem().toString();

            u=1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
