package com.easy.sumit.lifeline.ActivityHandlers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.CheckUsernameBackgroundWorker;

public class PreRegisterActivity extends AppCompatActivity implements AsyncResponse{

    private EditText user_name,user_mail,user_pass,user_pass_conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preregister);
        user_name= (EditText) findViewById(R.id.user_name_register);
        user_mail= (EditText) findViewById(R.id.user_mail_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);
        user_pass_conf= (EditText) findViewById(R.id.user_pass_conf_register);

    }
    public void onPreRegister(View view){
        if(!user_name.getText().toString().equals("") &&
                !user_mail.getText().toString().equals("") &&
                !user_pass.getText().toString().equals("") &&
                !user_pass_conf.getText().toString().equals("")){

            if(user_pass.getText().toString().equals(user_pass_conf.getText().toString())){
                CheckUsernameBackgroundWorker checkUsernameBackgroundWorker=
                        new CheckUsernameBackgroundWorker(this);
                checkUsernameBackgroundWorker.execute(user_name.getText().toString());
            }
            else{
                Toast.makeText(this,"Passwords not match",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog;
        if(output.equals("Successful")) {
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage(output);
            alertDialog.show();

            Intent intent = new Intent(this, RegisterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_name", user_name.getText().toString());
            bundle.putString("user_mail", user_mail.getText().toString());
            bundle.putString("user_pass", user_pass.getText().toString());
            intent.putExtras(bundle);

            startActivity(intent);
        }

        else{
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage(output);
            alertDialog.show();
        }
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}