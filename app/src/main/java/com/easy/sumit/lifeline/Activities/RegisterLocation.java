package com.easy.sumit.lifeline.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RemoteLocationRetriever;
import com.easy.sumit.lifeline.utils.Constants;

import java.util.ArrayList;

public class RegisterLocation extends AppCompatActivity{

    private Spinner stateSpinner;
    private Spinner districtSpinner;
    private Spinner subdistrictSpinner;
    private Button buttonSearch;

    private ArrayAdapter arrayAdapter;

    private RemoteLocationRetriever remoteLocationRetriever;

    private String user_name;
    private String user_mail;
    private String user_pass;
    private String state="";
    private String district="";
    private String sub_district="";
    private ArrayList<String> arrayList;
    private int s=0;
    private int d=0;
    private int u=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        districtSpinner = (Spinner) findViewById(R.id.districtSpinner);
        subdistrictSpinner = (Spinner) findViewById(R.id.subdistrictSpinner);
        buttonSearch = (Button) findViewById(R.id.submitButton);
        remoteLocationRetriever=new RemoteLocationRetriever(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_name = bundle.getString(Constants.USER_NAME);
            user_mail = bundle.getString(Constants.USER_MAIL);
            user_pass = bundle.getString(Constants.USER_PASS);
        }

        arrayList = new ArrayList<>();
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.state,
                android.R.layout.simple_dropdown_item_1line);
        stateSpinner.setAdapter(arrayAdapter);

        stateSpinner.setOnItemSelectedListener(new StateEventListener(this));
        districtSpinner.setOnItemSelectedListener(new DistrictEventListener(this));
        subdistrictSpinner.setOnItemSelectedListener(new SubDistrictEventListener(this));

        buttonSearch.setOnClickListener(view -> {
            if(u==1){
                startRegisterActivity();
            }
            else{
                Snackbar.make(view,
                              "Please select location properly",
                              Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }
    private void startRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_NAME,user_name);
        bundle.putString(Constants.USER_MAIL,user_mail);
        bundle.putString(Constants.USER_PASS,user_pass);
        bundle.putString(Constants.STATE,state);
        bundle.putString(Constants.DISTRICT,district);
        bundle.putString(Constants.SUB_DISTRICT,sub_district);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    class StateEventListener implements AdapterView.OnItemSelectedListener {
        RegisterLocation registerLocation;
        public StateEventListener(RegisterLocation registerLocation){
            this.registerLocation=registerLocation;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            state=stateSpinner.getSelectedItem().toString();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,user_name);
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
    class DistrictEventListener implements AdapterView.OnItemSelectedListener {
        RegisterLocation registerLocation;
        public DistrictEventListener(RegisterLocation registerLocation){
            this.registerLocation=registerLocation;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            district=districtSpinner.getSelectedItem().toString();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,user_name);
            bundle.putString("db_action","1");
            bundle.putString("location_level","3");
            bundle.putString("data",district);
            remoteLocationRetriever.updateData(subdistrictSpinner,bundle);
            remoteLocationRetriever.start();
            d=1;
            u=0;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    class SubDistrictEventListener implements AdapterView.OnItemSelectedListener {
        RegisterLocation registerLocation;
        public SubDistrictEventListener(RegisterLocation registerLocation){
            this.registerLocation=registerLocation;
        }
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sub_district=subdistrictSpinner.getSelectedItem().toString();

            u=1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}