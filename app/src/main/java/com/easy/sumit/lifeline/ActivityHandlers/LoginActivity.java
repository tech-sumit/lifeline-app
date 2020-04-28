package com.easy.sumit.lifeline.ActivityHandlers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.LoginBackgroundWorker;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    private EditText user_name,user_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_name= (EditText) findViewById(R.id.user_name_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);

    }

    public void onLogin(View view){
        String user_name_text = user_name.getText().toString();
        String user_pass_text = user_pass.getText().toString();

        LoginBackgroundWorker loginBackgroundWorker=new LoginBackgroundWorker(this);
        loginBackgroundWorker.execute(user_name_text, user_pass_text);

    }

    public void onRegister(View view){
        Intent intent=new Intent(this,PreRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    public void processFinish(String output) {
        AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Login");
        alertDialog.setMessage(output);
        alertDialog.show();
    }
}
