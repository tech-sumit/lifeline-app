package com.easy.sumit.lifeline.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
import com.easy.sumit.lifeline.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PreRegisterActivity extends AppCompatActivity{

    private EditText user_name,user_mail,user_pass,user_pass_conf;
    private Person person;
    String check_username_url = "http://10.0.2.2:9090/lifeline_app/check_username.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preregister);
        user_name= (EditText) findViewById(R.id.user_name_register);
        user_mail= (EditText) findViewById(R.id.user_mail_register);
        user_pass= (EditText) findViewById(R.id.user_pass_register);
        user_pass_conf= (EditText) findViewById(R.id.user_pass_conf_register);
        person=new Person();
    }
    public void onPreRegister(View view){
        if(!user_name.getText().toString().equals("") &&
                !user_mail.getText().toString().equals("") &&
                !user_pass.getText().toString().equals("") &&
                !user_pass_conf.getText().toString().equals("")){

            if(user_pass.getText().toString().equals(user_pass_conf.getText().toString())){

                StringRequest stringRequest=new StringRequest(Request.Method.POST, check_username_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Successful")) {
                                Toast.makeText(PreRegisterActivity.this,""+response,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(PreRegisterActivity.this, RegisterLocation.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.USER_NAME, user_name.getText().toString());
                                bundle.putString(Constants.USER_MAIL, user_mail.getText().toString());
                                bundle.putString(Constants.USER_PASS, user_pass.getText().toString());
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }

                            else{
                                Snackbar.make(getCurrentFocus(),""+response,Snackbar.LENGTH_SHORT);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map stringMap=new HashMap<>();
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
        }
        else {
            Snackbar.make(view,
                          "Please fill all fields",
                          Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}