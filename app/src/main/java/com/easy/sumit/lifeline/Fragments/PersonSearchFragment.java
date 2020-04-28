package com.easy.sumit.lifeline.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.RemoteDataRetriever;

public class PersonSearchFragment extends Fragment implements AsyncResponse{

    private String user_name;

    private Spinner personBloodGroup;
    private Button buttonSearch;
    private View view;
    private ArrayAdapter<CharSequence> arrayAdapterBloodGroup;
    private String bloodGroup="";

    public PersonSearchFragment(){

    }
    @SuppressLint("ValidFragment")
    public PersonSearchFragment(View view){
        this.view=view;
    }

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

        arrayAdapterBloodGroup = ArrayAdapter.
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
        RemoteDataRetriever remoteDataRetriever=new RemoteDataRetriever(this);
        buttonSearch.setOnClickListener(view1 -> {
            if(!bloodGroup.equals("")) {
                remoteDataRetriever.execute(user_name, "2","1", bloodGroup);
            }
            else{
                Toast.makeText(getActivity(),"Please select blood group",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void processFinish(String output) {
        ResultFragment resultFragment=new ResultFragment();
        Bundle bundle=new Bundle();
        bundle.putString("output",output);
        resultFragment.setArguments(bundle);
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.personSearchFragment,resultFragment).commit();
        buttonSearch.setVisibility(View.INVISIBLE);
        buttonSearch.setClickable(false);
        personBloodGroup.setVisibility(View.INVISIBLE);
        personBloodGroup.setClickable(false);
    }

}