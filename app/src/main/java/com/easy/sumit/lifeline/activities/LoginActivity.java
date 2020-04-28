package com.easy.sumit.lifeline.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.easy.sumit.lifeline.datamodal.Person;
import com.easy.sumit.lifeline.datamodal.URLList;
import com.easy.sumit.lifeline.backgroundworkers.LoginBackgroundWorker;

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
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                View passwordChangeView=View.inflate(LoginActivity.this,R.layout.forget_password_layout,null);
                builder.setView(passwordChangeView);
                builder.setCancelable(true);
                final AlertDialog forgotPasswordDialog=builder.create();
                final String[] sec_question = {""};

                final EditText user_name_edittext= (EditText) passwordChangeView.findViewById(R.id.user_name_edittext);
                final EditText sec_question_answer_edittext=(EditText) passwordChangeView.findViewById(R.id.sec_question_answer_edittext);
                final EditText pass_text=(EditText) passwordChangeView.findViewById(R.id.pass_text);
                final EditText conf_pass_text=(EditText) passwordChangeView.findViewById(R.id.conf_pass_text);
                Button verify_button= (Button) passwordChangeView.findViewById(R.id.verify_button);
                Button update_password_button=(Button) passwordChangeView.findViewById(R.id.update_password_button);

                final LinearLayout forgot_password1= (LinearLayout) passwordChangeView.findViewById(R.id.forgot_password1);
                final LinearLayout forgot_password2= (LinearLayout) passwordChangeView.findViewById(R.id.forgot_password2);

                final Spinner sec_questionSpinner= (Spinner) passwordChangeView.findViewById(R.id.sec_questionSpinner);
                ArrayAdapter<CharSequence> arrayAdapterSecQuestion= ArrayAdapter.createFromResource(LoginActivity.this,
                        R.array.sec_questions,
                        R.layout.support_simple_spinner_dropdown_item);
                sec_questionSpinner.setAdapter(arrayAdapterSecQuestion);
                sec_questionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sec_question[0] =sec_questionSpinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                verify_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!user_name_edittext.getText().toString().equals("")&&
                                !sec_question_answer_edittext.getText().toString().equals("")&&
                                !sec_question[0].equals("")){
                            String url = URLList.getUrl(LoginActivity.this,"getData");
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response!=null){
                                        Toast.makeText(LoginActivity.this,""+response,Toast.LENGTH_SHORT).show();
                                        if(response.equals("User verified")){
                                            forgot_password1.setVisibility(View.GONE);
                                            forgot_password2.setVisibility(View.VISIBLE);
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Unknown responce",Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i("Responce", ""+response);
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
                                    stringMap.put("user_name", "" + user_name_edittext.getText().toString());
                                    stringMap.put("db_action", "11");
                                    stringMap.put("total_data", "2");
                                    stringMap.put("sec_question", sec_question[0]);
                                    stringMap.put("sec_answer", sec_question_answer_edittext.getText().toString());
                                    return stringMap;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                            requestQueue.add(stringRequest);

                        }else{
                            Toast.makeText(LoginActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                update_password_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!pass_text.getText().toString().equals("")){
                            if(pass_text.getText().toString().length()>5){
                                if(!conf_pass_text.getText().toString().equals("")) {
                                    if(pass_text.getText().toString().equals(conf_pass_text.getText().toString())) {
                                        String url = URLList.getUrl(LoginActivity.this,"getData");
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(LoginActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                                                forgotPasswordDialog.cancel();
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
                                                stringMap.put("user_name", "" + user_name_edittext.getText().toString());
                                                stringMap.put("db_action", "10");
                                                stringMap.put("total_data", "1");
                                                stringMap.put("new_password", "" + pass_text.getText().toString());
                                                return stringMap;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                                        requestQueue.add(stringRequest);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Passwords not match", Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(LoginActivity.this, "Please re-enter password", Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                Toast.makeText(LoginActivity.this, "Password length must be greater than 5", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(LoginActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                forgotPasswordDialog.show();
            }
        });
    }

    public void onLogin(View view){
        user_name.setText(user_name.getText().toString().trim());
        user_name.setText(user_name.getText().toString().replaceAll(" ",""));
        user_name.setText(user_name.getText().toString().toLowerCase());
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
