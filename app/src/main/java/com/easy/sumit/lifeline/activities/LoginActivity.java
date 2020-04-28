package com.easy.sumit.lifeline.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.LoginBackgroundWorker;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    private EditText user_name,user_pass;
    private TextView forgetPassword;
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
        forgetPassword= (TextView) findViewById(R.id.forgot_password);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View passwordChangeView=View.inflate(LoginActivity.this,R.layout.update_password_layout,null);
                final Dialog changePasswordDialog=new Dialog(LoginActivity.this);
                changePasswordDialog.setContentView(passwordChangeView);
                final EditText oldPassword= (EditText) changePasswordDialog.findViewById(R.id.old_password);
                final EditText newPassword= (EditText) changePasswordDialog.findViewById(R.id.new_password);
                final EditText confPassword= (EditText) changePasswordDialog.findViewById(R.id.conf_password);
                Button updatePassword= (Button) changePasswordDialog.findViewById(R.id.updatePassword);
                changePasswordDialog.show();
                updatePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!oldPassword.getText().toString().equals("")){
                            if(!newPassword.getText().toString().equals("")){
                                if(!confPassword.getText().toString().equals("")){
                                    if(newPassword.getText().toString().equals(""+confPassword.getText().toString())){
                                        if(!(newPassword.getText().toString().length() <5)) {
                                            String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(LoginActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                                                    changePasswordDialog.cancel();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    error.printStackTrace();
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> stringMap = new HashMap<>();
                                                    stringMap.put("user_name", "" + person.getUser_name());
                                                    stringMap.put("db_action", "5");
                                                    stringMap.put("total_data", "2");
                                                    stringMap.put("old_password", "" + oldPassword.getText().toString());
                                                    stringMap.put("new_password", "" + newPassword.getText().toString());
                                                    return stringMap;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                                            requestQueue.add(stringRequest);
                                        }else{
                                            Toast.makeText(LoginActivity.this,"Password length is short",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Passwords not match",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(LoginActivity.this,"Confirm password",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"Enter new password",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this,"Enter old password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void onLogin(View view){
        user_name.setText(user_name.getText().toString().trim());
        user_pass.setText(user_pass.getText().toString().trim());
        if(user_name.getText().toString().isEmpty()||user_pass.getText().toString().isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
        }else{
            user_name.setText(user_name.getText().toString().replaceAll("'",""));
            user_pass.setText(user_pass.getText().toString().replaceAll("'",""));
            person.setUser_name(user_name.getText().toString());
            person.setUser_pass(user_pass.getText().toString());
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Authenticating, Please Wait.");
            progressDialog.show();
            LoginBackgroundWorker loginBackgroundWorker=new LoginBackgroundWorker(this,person);
            loginBackgroundWorker.start();
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle=new Bundle();
        bundle.putString("user_name",user_name.getText().toString());
        bundle.putString("user_pass",user_pass.getText().toString());
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user_name.setText(savedInstanceState.getString("user_name"));
        user_pass.setText(savedInstanceState.getString("user_pass"));
    }
}
