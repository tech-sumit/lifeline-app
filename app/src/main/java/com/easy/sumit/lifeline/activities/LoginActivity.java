package com.easy.sumit.lifeline.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.LoginBackgroundWorker;

public class LoginActivity extends AppCompatActivity{

    private EditText user_name,user_pass;
    private boolean login_status;
    private String personName="";
    public ProgressDialog progressDialog;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        person=new Person();
        user_name= (EditText) findViewById(R.id.user_name_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    public void onLogin(View view){
        person.setUser_name(user_name.getText().toString());
        person.setUser_pass(user_pass.getText().toString());
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Authenticating, Please Wait.");
        progressDialog.show();
        LoginBackgroundWorker loginBackgroundWorker=new LoginBackgroundWorker(this,person);
        loginBackgroundWorker.start();
    }

    public void onRegister(View view){
        Intent intent=new Intent(this,PreRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
