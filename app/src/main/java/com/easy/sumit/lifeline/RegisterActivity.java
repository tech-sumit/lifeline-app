package com.easy.sumit.lifeline;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.easy.sumit.lifeline.BackgroundWorker.AsyncResponse;
import com.easy.sumit.lifeline.BackgroundWorker.RegisterBackgroundWorker;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse{

    private EditText person_name,person_age,person_address,person_contact;
    private Spinner person_blood_group,person_gender,person_hiv_status;
    private Button buttonRegister;

    private ArrayAdapter<CharSequence> arrayAdapterBloodGroup;
    private ArrayAdapter<CharSequence> arrayAdapterGender;
    private ArrayAdapter<CharSequence> arrayAdapterHivStatus;

    private String name="",
            blood_group="",
            gender="",
            age="",
            hiv_status="",
            address="",
            contact_no="",
            user_name="",
            user_mail="",
            user_pass="",
            lat="",
            lon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        person_name= (EditText) findViewById(R.id.person_name);
        person_age= (EditText) findViewById(R.id.person_age);
        person_address= (EditText) findViewById(R.id.person_address);
        person_contact= (EditText) findViewById(R.id.person_contact);

        person_blood_group= (Spinner) findViewById(R.id.person_blood_group);
        person_gender= (Spinner) findViewById(R.id.person_gender);
        person_hiv_status= (Spinner) findViewById(R.id.person_hiv_status);

        buttonRegister= (Button) findViewById(R.id.buttonRegister2);

        initArrayAdapter();

        person_blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                blood_group=person_blood_group.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        person_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender=person_gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        person_hiv_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hiv_status=person_hiv_status.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    private void initArrayAdapter() {
        arrayAdapterBloodGroup=ArrayAdapter.createFromResource(this,
                R.array.blood_groups,
                R.layout.support_simple_spinner_dropdown_item);
        person_blood_group.setAdapter(arrayAdapterBloodGroup);

        arrayAdapterGender=ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.support_simple_spinner_dropdown_item);
        person_gender.setAdapter(arrayAdapterGender);

        arrayAdapterHivStatus=ArrayAdapter.createFromResource(this,
                R.array.hiv_status,
                R.layout.support_simple_spinner_dropdown_item);
        person_hiv_status.setAdapter(arrayAdapterHivStatus);
    }

    public void onRegister(View view){
        RegisterBackgroundWorker registerBackgroundWorker=new RegisterBackgroundWorker(this,this);
        registerBackgroundWorker.execute();
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register");
        alertDialog.setMessage(output);
        alertDialog.show();
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}