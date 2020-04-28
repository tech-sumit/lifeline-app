package com.easy.sumit.lifeline.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.AsyncResponse;
import com.easy.sumit.lifeline.utils.LoginBackgroundWorker;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    private EditText user_name,user_pass;
    private boolean login_status;
    private String personName="";
    private AlertDialog alertDialog;

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
        loginBackgroundWorker.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,user_name_text, user_pass_text);

    }

    public void onRegister(View view){
        Intent intent=new Intent(this,PreRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String output) {
        alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Login");
        alertDialog.setMessage(output);
        alertDialog.show();
        if(!output.equals("")){
            login_status=checkLogin(output);
            if(login_status){
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("user_name",user_name.getText().toString());
                bundle.putString("personName",personName);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.i("LOGIN","Login Success");
            }
            else{
                Log.i("LOGIN","Login Failed");
            }
        }
        else{
            Log.e("Output Exception","Output String is null : "+output);
        }
    }

    private boolean checkLogin(String output){
        try{
            StringBuilder outputBuilder=new StringBuilder(output);
            if(outputBuilder.substring(0,13).equals("Login success")){
                personName=outputBuilder.substring(22);
                login_status=true;
            }
            else
            {
                login_status=false;
            }

        }catch(StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return login_status;
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

}
