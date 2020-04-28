package com.easy.sumit.lifeline.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.RemoteDataRetriever;
import com.easy.sumit.lifeline.utils.RemoteLocationRetriever;

public class PersonSearchFragment extends Fragment implements AsyncResponse,View.OnClickListener{

    private String user_name;

    private Spinner personBloodGroup;
    private Spinner stateSpinner;
    private Spinner districtSpinner;
    private Spinner sub_districtSpinner;
    private Button buttonSearch;
    private String bloodGroup="";
    private String state="";
    private String district="";
    private String sub_district="";
    private int d=0;
    private int s=0;
    private int u=0;
    private RemoteDataRetriever remoteDataRetriever;
    private RemoteLocationRetriever remoteLocationRetriever;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_name = getArguments().getString("user_name");

        }
        else{
            Log.e("ERROR","NO user_name recived as intent argument");
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
                Log.i("Blood Group Selected",bloodGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void processFinish(String output) {
        if(!output.equals("")) {
            ResultFragment resultFragment = new ResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString("output", output);
            resultFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.personSearchFragment, resultFragment)
                    .commit();

            /*fragmentManager.beginTransaction()
                    .add(resultFragment,"ResultFragment").commit();
            */
            buttonSearch.setVisibility(View.INVISIBLE);
            buttonSearch.setClickable(false);
            personBloodGroup.setVisibility(View.INVISIBLE);
            personBloodGroup.setClickable(false);
            remoteDataRetriever.cancel(true);
        }
        else{
            Snackbar.make(getView(),"Sorry, no data found",Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View view) {
        remoteDataRetriever=new RemoteDataRetriever(this);
        if(!bloodGroup.equals("")) {
            remoteDataRetriever.execute(user_name, "2","4", bloodGroup,state,district,sub_district);
        }
        else{
            Snackbar.make(view,
                          "Please select blood group",
                          Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    class StateEventListener implements AdapterView.OnItemSelectedListener{
        PersonSearchFragment personSearchFragment;
        public StateEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            state=stateSpinner.getSelectedItem().toString();
            Log.d("***INFO***","State:"+state);
            remoteLocationRetriever=new RemoteLocationRetriever(personSearchFragment.getActivity(),districtSpinner);
            remoteLocationRetriever.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,user_name,"1","2",state);

            s = 1;
            d=0;
            u=0;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    class DistrictEventListener implements AdapterView.OnItemSelectedListener {
        PersonSearchFragment personSearchFragment;
        public DistrictEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            district=districtSpinner.getSelectedItem().toString();
            Log.d("***INFO***","District:"+district);
            remoteLocationRetriever=new RemoteLocationRetriever(personSearchFragment.getActivity(),sub_districtSpinner);
            remoteLocationRetriever.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,user_name,"1","3",district);

            d=1;
            u=0;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    class SubDistrictEventListener implements AdapterView.OnItemSelectedListener {
        PersonSearchFragment personSearchFragment;
        public SubDistrictEventListener(PersonSearchFragment personSearchFragment){
            this.personSearchFragment=personSearchFragment;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d("***INFO***","Sub district:"+sub_district);
            sub_district=sub_districtSpinner.getSelectedItem().toString();

            u=1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}