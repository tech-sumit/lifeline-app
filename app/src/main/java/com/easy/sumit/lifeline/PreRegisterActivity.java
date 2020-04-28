package com.easy.sumit.lifeline;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easy.sumit.lifeline.BackgroundWorker.AsyncResponse;
import com.easy.sumit.lifeline.BackgroundWorker.CheckUsernameBackgroundWorker;

public class PreRegisterActivity extends AppCompatActivity implements AsyncResponse{

    private EditText user_name,user_mail,user_pass,user_pass_conf;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preregister);
        user_name= (EditText) findViewById(R.id.user_name_register);
        user_mail= (EditText) findViewById(R.id.user_mail_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);
        user_pass_conf= (EditText) findViewById(R.id.user_pass_conf_register);
        buttonRegister= (Button) findViewById(R.id.buttonRegister1);

    }
    public void onPreRegister(View view){
        if(!user_name.getText().toString().equals("") &&
                !user_mail.getText().toString().equals("") &&
                !user_pass.getText().toString().equals("") &&
                !user_pass_conf.getText().toString().equals("")){

            if(user_pass.getText().toString().equals(user_pass_conf.getText().toString())){
                CheckUsernameBackgroundWorker checkUsernameBackgroundWorker=
                        new CheckUsernameBackgroundWorker(this,this);
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
        if(output.equals("Successfull")){
            AlertDialog alertDialog= new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage(output);
            alertDialog.show();
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }
        else{

            AlertDialog alertDialog= new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Register");
            alertDialog.setMessage(output);
            alertDialog.show();
        }
    }
}