package com.easy.sumit.lifeline.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.RemoteLocationRetriever;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterLocation extends AppCompatActivity{

    Spinner stateSpinner;
    Spinner districtSpinner;
    Spinner subdistrictSpinner;
    Button buttonSearch;

    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayAdapter arrayAdapter;

    RemoteLocationRetriever remoteLocationRetriever;

    String user_name;
    String user_mail;
    String user_pass;
    String state="";
    String district="";
    String sub_district="";
    ArrayList<String> arrayList;
    int s=0,d=0,u=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        districtSpinner = (Spinner) findViewById(R.id.districtSpinner);
        subdistrictSpinner = (Spinner) findViewById(R.id.subdistrictSpinner);
        buttonSearch = (Button) findViewById(R.id.submitButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_name = bundle.getString("user_name");
            user_mail = bundle.getString("user_mail");
            user_pass = bundle.getString("user_pass");
        }

        arrayList = new ArrayList<String>();
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.state,
                android.R.layout.simple_dropdown_item_1line);
        stateSpinner.setAdapter(arrayAdapter);

        stateSpinner.setOnItemSelectedListener(new StateEventListener(this));
        districtSpinner.setOnItemSelectedListener(new DistrictEventListener(this));
        subdistrictSpinner.setOnItemSelectedListener(new SubDistrictEventListener(this));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(u==1){
                    startRegisterActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Please select location properly",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    void startRegisterActivity(){
        Log.i("State:",""+state);
        Log.i("District:",""+district);
        Log.i("Sub_District:",""+sub_district);
        Intent intent = new Intent(this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_name",user_name);
        bundle.putString("user_mail",user_mail);
        bundle.putString("user_pass",user_pass);
        bundle.putString("state",state);
        bundle.putString("district",district);
        bundle.putString("sub_district",sub_district);
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
            Log.d("***INFO***","State:"+state);
            remoteLocationRetriever=new RemoteLocationRetriever(registerLocation,districtSpinner);
            remoteLocationRetriever.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,user_name,"1","2",state);

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
            Log.d("***INFO***","District:"+district);
            remoteLocationRetriever=new RemoteLocationRetriever(registerLocation,subdistrictSpinner);
            remoteLocationRetriever.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,user_name,"1","3",district);

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
            Log.d("***INFO***","Sub district:"+sub_district);
            sub_district=subdistrictSpinner.getSelectedItem().toString();

            u=1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}