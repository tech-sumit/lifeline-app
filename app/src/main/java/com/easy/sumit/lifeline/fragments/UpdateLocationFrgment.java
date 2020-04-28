package com.easy.sumit.lifeline.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RemoteLocationRetriever;
import com.easy.sumit.lifeline.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class UpdateLocationFrgment extends Fragment {

    String state="";
    String district="";
    String sub_district="";
    Person person;
    ProfileFragment profileFragment;
    int s=0;
    int d=0;
    int u=0;

    public UpdateLocationFrgment() {}

    public static UpdateLocationFrgment newInstance(Person person, ProfileFragment profileFragment) {
        UpdateLocationFrgment fragment = new UpdateLocationFrgment();
        fragment.person=person;
        fragment.profileFragment=profileFragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RemoteLocationRetriever remoteLocationRetriever=new RemoteLocationRetriever(getActivity());
        final Button updateLocarion = (Button) view.findViewById(R.id.updateLocation);
        final Spinner stateSpinner= (Spinner) view.findViewById(R.id.updateStateSpinner);
        final Spinner districtSpinner= (Spinner) view.findViewById(R.id.updateDistrictSpinner);
        final Spinner subdistrictSpinner= (Spinner) view.findViewById(R.id.updateSubdistrictSpinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.state,
                android.R.layout.simple_dropdown_item_1line);
        stateSpinner.setAdapter(arrayAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state=stateSpinner.getSelectedItem().toString();
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district=districtSpinner.getSelectedItem().toString();
                Bundle bundle=new Bundle();
                bundle.putString(Constants.USER_NAME,person.getUser_name());
                bundle.putString("db_action","1");
                bundle.putString("location_level","3");
                bundle.putString("data",district);
                remoteLocationRetriever.updateData(subdistrictSpinner,bundle);
                remoteLocationRetriever.start();
                d=1;
                u=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subdistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_district=subdistrictSpinner.getSelectedItem().toString();
                u=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateLocarion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!state.equals("")||!district.equals("")||!sub_district.equals("")) {
                    String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                            person.setState(state);
                            person.setDistrict(district);
                            person.setSub_district(sub_district);
                            person.updatePreferences(getContext());
                            profileFragment.personState.setText("State: "+person.getState());
                            profileFragment.personDistrict.setText("District: "+person.getDistrict());
                            profileFragment.personSubdistrict.setText("Sub district: "+person.getSub_district());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> stringMap = new HashMap<>();
                            stringMap.put("user_name", "" + person.getUser_name());
                            stringMap.put("db_action", "7");
                            stringMap.put("total_data", "3");
                            stringMap.put("state", state);
                            stringMap.put("district", district);
                            stringMap.put("sub_district", sub_district);
                            return stringMap;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(getContext(),"Select valid location",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        profileFragment.fab.setVisibility(View.VISIBLE);
    }
}
