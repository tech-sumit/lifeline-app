package com.easy.sumit.lifeline.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.easy.sumit.lifeline.R;

public class SecurityActivity extends AppCompatActivity {
    private static final String NAME = "name";
    private String mName;
    private TextView textView;
    private int previousState=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        mName=getIntent().getStringExtra("NAME");
        this.setFinishOnTouchOutside(false);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        textView= (TextView)findViewById(R.id.nameView);
        textView.setText(""+mName);
        ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
                .listen(new PhoneEndListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("**SecurityActivity","Activity Started");
    }

    class PhoneEndListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    //Hangup
                    case TelephonyManager.CALL_STATE_IDLE:
                        previousState = TelephonyManager.CALL_STATE_IDLE;
                        Log.i("Call Status", "CALL_STATE_IDLE");
                        break;
                    //Outgoing
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if (previousState == TelephonyManager.CALL_STATE_RINGING) {
                            Log.i("**SecurityActivity", "Activity Closed");

                        }
                        previousState = TelephonyManager.CALL_STATE_OFFHOOK;
                        Log.i("Call Status", "CALL_STATE_OFFHOOK");
                        break;
                    //Incoming
                    case TelephonyManager.CALL_STATE_RINGING:
                        previousState = TelephonyManager.CALL_STATE_RINGING;
                        Log.i("Call Status", "CALL_STATE_RINGING");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
