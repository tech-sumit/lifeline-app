package com.easy.sumit.lifeline.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.RegisterBackgroundWorker;
import com.easy.sumit.lifeline.utils.Constants;

public class RegisterActivity extends AppCompatActivity{

    private EditText person_name, person_age, person_address, person_contact,person_last_donated;
    private Spinner person_blood_group, person_gender;
    private Person person=new Person();
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        person_name = (EditText) findViewById(R.id.person_name);
        person_age = (EditText) findViewById(R.id.person_age);
        person_address = (EditText) findViewById(R.id.person_address);
        person_contact = (EditText) findViewById(R.id.person_contact);
        person_last_donated= (EditText) findViewById(R.id.person_last_donated);
        person_blood_group = (Spinner) findViewById(R.id.person_blood_group);
        person_gender = (Spinner) findViewById(R.id.person_gender);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            person.setUser_name(bundle.getString(Constants.USER_NAME));
            person.setUser_mail(bundle.getString(Constants.USER_MAIL));
            person.setUser_pass(bundle.getString(Constants.USER_PASS));
            person.setState(bundle.getString(Constants.STATE));
            person.setDistrict(bundle.getString(Constants.DISTRICT));
            person.setSub_district(bundle.getString(Constants.SUB_DISTRICT));
        }

        initSpinners();
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
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> arrayAdapterBloodGroup = ArrayAdapter.createFromResource(this,
                R.array.blood_groups,
                R.layout.support_simple_spinner_dropdown_item);
        person_blood_group.setAdapter(arrayAdapterBloodGroup);

        ArrayAdapter<CharSequence> arrayAdapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.support_simple_spinner_dropdown_item);
        person_gender.setAdapter(arrayAdapterGender);
    }

    public void onRegister(View view) {
        person.setName(person_name.getText().toString());
        person.setAge(person_age.getText().toString());
        person.setAddress(person_address.getText().toString());
        person.setContact_no(person_contact.getText().toString());
        int dd,mm,yyyy;
        try {
            if(!person_last_donated.getText().equals("")) {

                if (person_last_donated.length() > 10) {
                    Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                } else {
                    dd = Integer.parseInt(person_last_donated.getText().toString().substring(0, 2));
                    mm = Integer.parseInt(person_last_donated.getText().toString().substring(3, 5));
                    yyyy = Integer.parseInt(person_last_donated.getText().toString().substring(6, 10));
                    if (mm > 12 || dd > 31 || yyyy > 2018) {
                        Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
                        Log.i("Date", "DD:" + dd + "MM:" + mm + "YYYY:" + yyyy);
                        return;
                    }
                    if (mm == 2 && dd > 29) {
                        Toast.makeText(this, "Invalid day in february", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                person.setLast_donated(""+person_last_donated.getText().toString());
            }
            else{
                person.setLast_donated("N/A");
            }
        }catch (NumberFormatException e){
            person.setLast_donated("N/A");
            e.printStackTrace();
            Toast.makeText(this,"Enter valid date",Toast.LENGTH_SHORT).show();
            return;
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle=new Bundle();
        /*
            private EditText person_name, person_age, person_address, person_contact,person_last_donated;
            private Spinner person_blood_group, person_gender;
            private Person person;
         */
        bundle.putString(Constants.USER_NAME,person.getUser_name());
        bundle.putString(Constants.USER_MAIL,person.getUser_mail());
        bundle.putString(Constants.USER_PASS,person.getUser_pass());
        bundle.putString(Constants.STATE,person.getState());
        bundle.putString(Constants.DISTRICT,person.getDistrict());
        bundle.putString(Constants.SUB_DISTRICT,person.getSub_district());
        bundle.putString(Constants.NAME,person.getName());
        bundle.putString(Constants.AGE,person.getAge());
        bundle.putString(Constants.ADDRESS,person.getAddress());
        bundle.putString(Constants.CONTACT_NO,person.getContact_no());
        bundle.putString(Constants.LAST_DONATED,person.getLast_donated());
        bundle.putString(Constants.BLOOD_GROUP,person.getBlood_group());
        bundle.putString(Constants.GENDER,person.getGender());
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(person!=null) {
            person.setUser_name(savedInstanceState.getString(Constants.USER_NAME));
            person.setUser_mail(savedInstanceState.getString(Constants.USER_MAIL));
            person.setUser_pass(savedInstanceState.getString(Constants.USER_PASS));
            person.setState(savedInstanceState.getString(Constants.STATE));
            person.setDistrict(savedInstanceState.getString(Constants.DISTRICT));
            person.setName(savedInstanceState.getString(Constants.NAME));
            person.setAge(savedInstanceState.getString(Constants.AGE));
            person.setAddress(savedInstanceState.getString(Constants.ADDRESS));
            person.setContact_no(savedInstanceState.getString(Constants.CONTACT_NO));
            person.setLast_donated(savedInstanceState.getString(Constants.LAST_DONATED));
            person.setBlood_group(savedInstanceState.getString(Constants.BLOOD_GROUP));
            person.setGender(savedInstanceState.getString(Constants.GENDER));
        }
    }
}