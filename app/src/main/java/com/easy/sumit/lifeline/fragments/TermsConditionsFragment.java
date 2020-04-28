package com.easy.sumit.lifeline.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.activities.LoginActivity;
import com.easy.sumit.lifeline.activities.SplashActivity;
import com.easy.sumit.lifeline.datamodal.URLList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class TermsConditionsFragment extends Fragment {
    private Button acceptButton,declineButton;
    private TextView policy_link;
    private SplashActivity splashActivity;
    public TermsConditionsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_conditions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        policy_link = (TextView) view.findViewById(R.id.policy_link);
        acceptButton= (Button) view.findViewById(R.id.acceptButton);
        declineButton= (Button) view.findViewById(R.id.declineButton);
        policy_link.setText(Html.fromHtml("<u>By clicking Accept you agree Policies available at this link,</u>"));
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("lifeline_tnc", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("tncStatus","true");
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
        policy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                mFirebaseRemoteConfig.setDefaults(R.xml.url_config);
                mFirebaseRemoteConfig.fetch(60)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mFirebaseRemoteConfig.activateFetched();
                                Log.i("Firebase Config","L1: "+mFirebaseRemoteConfig.getString("getData")+
                                        "\nL2: "+mFirebaseRemoteConfig.getString("call_log")+
                                        "\nL3: "+mFirebaseRemoteConfig.getString("check_username")+
                                        "\nL4: "+mFirebaseRemoteConfig.getString("eula")+
                                        "\nL5: "+mFirebaseRemoteConfig.getString("getLocation")+
                                        "\nL6: "+mFirebaseRemoteConfig.getString("login")+
                                        "\nL7: "+mFirebaseRemoteConfig.getString("register")+
                                        "\nL8: "+mFirebaseRemoteConfig.getString("webpage"));
                            }
                        });

                AlertDialog.Builder policyDialog = new AlertDialog.Builder(getContext());
                policyDialog.setTitle("EULA");
                WebView policy_view = new WebView(getContext());
                policy_view.loadUrl(URLList.getUrl(getContext(),"eula"));
                policy_view.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(URLList.getUrl(getContext(),"eula"));
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                });
                policyDialog.setView(policy_view);
                policyDialog.setCancelable(true);
                policyDialog.show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}