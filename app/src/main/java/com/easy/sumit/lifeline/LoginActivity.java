package com.easy.sumit.lifeline;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.easy.sumit.lifeline.BackgroundWorker.AsyncResponse;
import com.easy.sumit.lifeline.BackgroundWorker.LoginBackgroundWorker;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    private EditText user_name,user_pass;
    private Button buttonLogin,buttonRegister;

    private String user_name_text="",user_pass_text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_name= (EditText) findViewById(R.id.user_name_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);

        buttonLogin= (Button) findViewById(R.id.buttonLogin);
        buttonRegister= (Button) findViewById(R.id.buttonRegister);

    }

    public void onLogin(View view){
        user_name_text=user_name.getText().toString();
        user_pass_text=user_pass.getText().toString();

        LoginBackgroundWorker loginBackgroundWorker=new LoginBackgroundWorker(this,this);
        loginBackgroundWorker.execute(user_name_text,user_pass_text);

    }

    public void onRegister(View view){
        Intent intent=new Intent(this,PreRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Login");
        alertDialog.setMessage(output);
        alertDialog.show();
    }
}
