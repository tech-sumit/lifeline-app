package com.easy.sumit.lifeline;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.easy.sumit.lifeline.BackgroundWorker.AsyncResponse;
import com.easy.sumit.lifeline.BackgroundWorker.RegisterBackgroundWorker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private EditText person_name, person_age, person_address, person_contact;
    private Spinner person_blood_group, person_gender, person_hiv_status;
    private Button buttonRegister;

    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationRequest mLocationRequest;

    private ArrayAdapter<CharSequence> arrayAdapterBloodGroup;
    private ArrayAdapter<CharSequence> arrayAdapterGender;
    private ArrayAdapter<CharSequence> arrayAdapterHivStatus;

    private String name = "",
            blood_group = "",
            gender = "",
            age = "",
            hiv_status = "",
            address = "",
            contact_no = "",
            user_name = "",
            user_mail = "",
            user_pass = "",
            lat = "",
            lon = "";


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

        user_name=savedInstanceState.getString("user_name");
        user_mail=savedInstanceState.getString("user_mail");
        user_pass=savedInstanceState.getString("user_pass");

        buttonRegister = (Button) findViewById(R.id.buttonRegister2);

        initArrayAdapter();
        initLocation();
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
        arrayAdapterBloodGroup = ArrayAdapter.createFromResource(this,
                R.array.blood_groups,
                R.layout.support_simple_spinner_dropdown_item);
        person_blood_group.setAdapter(arrayAdapterBloodGroup);

        arrayAdapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.support_simple_spinner_dropdown_item);
        person_gender.setAdapter(arrayAdapterGender);

        arrayAdapterHivStatus = ArrayAdapter.createFromResource(this,
                R.array.hiv_status,
                R.layout.support_simple_spinner_dropdown_item);
        person_hiv_status.setAdapter(arrayAdapterHivStatus);
    }

    private void initLocation() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    public void onRegister(View view) {
        name=person_name.getText().toString();
        age=person_age.getText().toString();
        address=person_address.getText().toString();
        contact_no=person_contact.getText().toString();

        RegisterBackgroundWorker registerBackgroundWorker = new RegisterBackgroundWorker(this, this);
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
                lat,
                lon);
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register");
        alertDialog.setMessage(output);
        alertDialog.show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
        }
        createLocationRequest();
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50000);
        mLocationRequest.setFastestInterval(25000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                mLocationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
    }
}
