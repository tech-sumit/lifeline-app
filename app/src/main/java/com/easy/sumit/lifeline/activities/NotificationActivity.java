package com.easy.sumit.lifeline.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.easy.sumit.lifeline.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private String user_name="";
    private TextView reportText;
    private RelativeLayout notiLayout1,notiLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        user_name=getIntent().getStringExtra("user_name");

        Button yesButton = (Button) findViewById(R.id.notiYES);
        Button noButton = (Button) findViewById(R.id.notiNO);
        Button reportButton = (Button) findViewById(R.id.notiReport);
        Button cancelButton = (Button) findViewById(R.id.notiCancel);
        reportText= (TextView) findViewById(R.id.notiText);
        notiLayout1= (RelativeLayout) findViewById(R.id.notiLayout1);
        notiLayout2= (RelativeLayout) findViewById(R.id.notiLayout2);

        notiLayout1.setVisibility(View.VISIBLE);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiLayout1.setVisibility(View.GONE);
                notiLayout2.setVisibility(View.VISIBLE);
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        "http://10.0.2.2:9090/lifeline_app/getData.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("BLOCK RESPONCE", "" + response);
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
                        stringMap.put("user_name", "" + user_name);
                        stringMap.put("db_action", "4");
                        stringMap.put("total_data", "0");
                        return stringMap;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
                requestQueue.add(stringRequest);
                String report = "";
                if (!report.equals("")) {
                    report = reportText.getText().toString();
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, "17.sumitagrawal@gmail.com");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LIFELINE USER REPORT");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "" + report);

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Mail Status", "Sent");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(NotificationActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        bundle.putString(Constants.USER_NAME,user_name);
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user_name=savedInstanceState.getString(Constants.USER_NAME);
    }
}
