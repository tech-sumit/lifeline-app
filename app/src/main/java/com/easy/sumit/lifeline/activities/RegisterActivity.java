package com.easy.sumit.lifeline.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RegisterBackgroundWorker;
import com.easy.sumit.lifeline.utils.Constants;

public class RegisterActivity extends AppCompatActivity{

    private EditText person_name, person_age, person_address, person_contact;
    private Spinner person_blood_group, person_gender, person_hiv_status;
    private Person person;
    public ProgressDialog progressDialog;

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
        person=new Person();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            person.setUser_name(bundle.getString(Constants.USER_NAME));
            person.setUser_mail(bundle.getString(Constants.USER_MAIL));
            person.setUser_pass(bundle.getString(Constants.USER_PASS));
            person.setState(bundle.getString(Constants.STATE));
            person.setDistrict(bundle.getString(Constants.DISTRICT));
            person.setSub_district(bundle.getString(Constants.SUB_DISTRICT));
        }

        initArrayAdapter();
        person_blood_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                person.setBlood_group(person_blood_group.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        person_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                person.setGender(person_gender.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        person_hiv_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                person.setHiv_status(person_hiv_status.getSelectedItem().toString());
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

    private String getIMEI(){
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        TelephonyManager telephonyManager= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void onRegister(View view) {
        person.setName(person_name.getText().toString());
        person.setAge(person_age.getText().toString());
        person.setAddress(person_address.getText().toString());
        person.setContact_no(person_contact.getText().toString());
        person.setImei_no(getIMEI());
        if(Integer.parseInt(person.getAge())>17){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please wait.");
            progressDialog.show();
            RegisterBackgroundWorker registerBackgroundWorker = new RegisterBackgroundWorker(this);
            registerBackgroundWorker.updateData(person);
            registerBackgroundWorker.start();
        }else{
            Snackbar.make(view,"Age must be at least 18 years",Snackbar.LENGTH_SHORT).show();
        }
    }
}