package com.easy.sumit.lifeline.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.RegisterBackgroundWorker;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse {

    private EditText person_name, person_age, person_address, person_contact;
    private Spinner person_blood_group, person_gender, person_hiv_status;
    private RegisterBackgroundWorker registerBackgroundWorker;
    private String blood_group = "";
    private String gender = "";
    private String hiv_status = "";
    private String user_name = "";
    private String user_mail = "";
    private String user_pass = "";
    private String state="";
    private String district="";
    private String sub_district="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        person_name = (EditText) findViewById(R.id.person_name);
        person_age = (EditText) findViewById(R.id.person_age);
        person_address = (EditText) findViewById(R.id.person_address);
        person_contact = (EditText) findViewById(R.id.person_contact);

        person_blood_group = (Spinner) findViewById(R.id.person_blood_group);
        person_gender = (Spinner) findViewById(R.id.person_gender);
        person_hiv_status = (Spinner) findViewById(R.id.person_hiv_status);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            user_name = bundle.getString("user_name");
            user_mail = bundle.getString("user_mail");
            user_pass = bundle.getString("user_pass");
            state = bundle.getString("state");
            district= bundle.getString("district");
            sub_district = bundle.getString("sub_district");
        }

        initArrayAdapter();
        person_blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                blood_group = person_blood_group.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        person_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = person_gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        person_hiv_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hiv_status = person_hiv_status.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void initArrayAdapter() {
        ArrayAdapter<CharSequence> arrayAdapterBloodGroup = ArrayAdapter.createFromResource(this,
                R.array.blood_groups,
                R.layout.support_simple_spinner_dropdown_item);
        person_blood_group.setAdapter(arrayAdapterBloodGroup);

        ArrayAdapter<CharSequence> arrayAdapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.support_simple_spinner_dropdown_item);
        person_gender.setAdapter(arrayAdapterGender);

        ArrayAdapter<CharSequence> arrayAdapterHivStatus = ArrayAdapter.createFromResource(this,
                R.array.hiv_status,
                R.layout.support_simple_spinner_dropdown_item);
        person_hiv_status.setAdapter(arrayAdapterHivStatus);
    }


    public void onRegister(View view) {
        String name = person_name.getText().toString();
        String age = person_age.getText().toString();
        String address = person_address.getText().toString();
        String contact_no = person_contact.getText().toString();

        registerBackgroundWorker = new RegisterBackgroundWorker(this);
        registerBackgroundWorker.execute(name,
                blood_group,
                gender,
                age,
                hiv_status,
                address,
                contact_no,
                user_name,
                user_mail,
                user_pass,
                state,
                district,
                sub_district);
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register");
        alertDialog.setMessage(output);
        alertDialog.show();
        registerBackgroundWorker.cancel(true);
        Log.i("Output:",""+output);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}