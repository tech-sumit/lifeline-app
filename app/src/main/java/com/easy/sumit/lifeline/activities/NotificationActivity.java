package com.easy.sumit.lifeline.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private String user_name="";
    private Button yesButton,noButton,reportButton,cancelButton;
    private TextView reportText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        user_name=getIntent().getStringExtra("user_name");
        yesButton= (Button) findViewById(R.id.notiYES);
        noButton= (Button) findViewById(R.id.notiNO);
        reportButton= (Button) findViewById(R.id.notiReport);
        cancelButton= (Button) findViewById(R.id.notiCancel);
        reportText= (TextView) findViewById(R.id.notiText);

        reportButton.setEnabled(false);
        reportText.setEnabled(false);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesButton.setEnabled(false);
                noButton.setEnabled(false);
                reportText.setEnabled(true);
                reportButton.setEnabled(true);

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
                        Map<String, String> stringMap = new HashMap<String, String>();
                        stringMap.put("user_name", "" + user_name);
                        stringMap.put("db_action", "4");
                        stringMap.put("total_data", "0");
                        return stringMap;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
