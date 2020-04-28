package com.easy.sumit.lifeline.utils.Notifiaction;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDServiceAdapter extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIDService";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
    }
}
