package com.easy.sumit.lifeline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PreRegisterActivity extends AppCompatActivity{

    private EditText user_name,user_mail,user_pass,user_pass_conf,text_sec_answer;
    private Spinner secQuestionSpinner;
    private String check_username_url = "http://10.0.2.2:9090/lifeline_app/check_username.php";
    private String sec_question="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preregister);
        user_name= (EditText) findViewById(R.id.user_name_register);
        user_mail= (EditText) findViewById(R.id.user_mail_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);
        user_pass_conf= (EditText) findViewById(R.id.user_pass_conf_register);
        secQuestionSpinner= (Spinner) findViewById(R.id.spinnerSecQuestion);
        text_sec_answer= (EditText) findViewById(R.id.text_sec_answer);
        ArrayAdapter<CharSequence> arrayAdapterSecQuestion= ArrayAdapter.createFromResource(this,
                R.array.sec_questions,
                R.layout.support_simple_spinner_dropdown_item);
        secQuestionSpinner.setAdapter(arrayAdapterSecQuestion);
        secQuestionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sec_question=secQuestionSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void onPreRegister(View view){
        if(!user_name.getText().toString().equals("") &&
                !user_mail.getText().toString().equals("") &&
                !user_pass.getText().toString().equals("") &&
                !user_pass_conf.getText().toString().equals("")&&
                !sec_question.equals("")&&
                !text_sec_answer.getText().toString().equals("")){

            if(user_name.getText().toString().length()>6 &&
                    user_pass.getText().toString().length()>6){

                if(user_pass.getText().toString().equals(user_pass_conf.getText().toString())){

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, check_username_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("Successful")) {
                                Toast.makeText(PreRegisterActivity.this, "" + response, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(PreRegisterActivity.this, RegisterLocation.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.USER_NAME, user_name.getText().toString());
                                bundle.putString(Constants.USER_MAIL, user_mail.getText().toString());
                                bundle.putString(Constants.USER_PASS, user_pass.getText().toString());
                                bundle.putString(Constants.SEC_QUESTION, sec_question);
                                bundle.putString(Constants.SEC_ANSWER, text_sec_answer.getText().toString());

                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(PreRegisterActivity.this,""+response,Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(PreRegisterActivity.this, "Connection Failed", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> stringMap=new HashMap<>();
                            try {
                                stringMap.put(URLEncoder.encode("user_name", "UTF-8"),
                                        URLEncoder.encode(user_name.getText().toString(), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return stringMap;
                        }
                    };
                    RequestQueue requestQueue= Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }
                else{
                    Snackbar.make(view,
                            "Passwords not match",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }else {
                Snackbar.make(view,
                        "username or password length must greater than 6 characters",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        else {
            Snackbar.make(view,
                          "Please fill all fields",
                          Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        /*
        private EditText user_name,user_mail,user_pass,user_pass_conf;
         */
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_NAME,user_name.getText().toString());
        bundle.putString(Constants.USER_MAIL,user_mail.getText().toString());
        bundle.putString(Constants.USER_PASS,user_pass.getText().toString());
        bundle.putString("USER_PASS_CONF",user_pass_conf.getText().toString());
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user_name.setText(savedInstanceState.getString(Constants.USER_NAME));
        user_mail.setText(savedInstanceState.getString(Constants.USER_MAIL));
        user_pass.setText(savedInstanceState.getString(Constants.USER_PASS));
        user_pass_conf.setText(savedInstanceState.getString("USER_PASS_CONF"));
    }
}
