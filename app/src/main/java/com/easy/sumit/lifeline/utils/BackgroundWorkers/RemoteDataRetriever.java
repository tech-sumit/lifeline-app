package com.easy.sumit.lifeline.utils.BackgroundWorkers;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.Fragments.PersonSearchFragment;
import com.easy.sumit.lifeline.Fragments.ResultFragment;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class RemoteDataRetriever{

    private PersonSearchFragment personSearchFragment;
    private Bundle bundle;

    public RemoteDataRetriever(PersonSearchFragment personSearchFragment) {
        this.personSearchFragment=personSearchFragment;
    }
    public void updateData(Bundle bundle){
        this.bundle=bundle;
    }
    public void start(){
        String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("")) {
                    ResultFragment resultFragment = new ResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("output",response);
                    resultFragment.setArguments(bundle);
                    FragmentManager fragmentManager = personSearchFragment.getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.personSearchFragment, resultFragment)
                            .commit();

                    personSearchFragment.buttonSearch.setVisibility(View.INVISIBLE);
                    personSearchFragment.buttonSearch.setClickable(false);
                    personSearchFragment.personBloodGroup.setVisibility(View.INVISIBLE);
                    personSearchFragment.personBloodGroup.setClickable(false);
                }
                else{
                    Snackbar.make(personSearchFragment.getView(),"Sorry, no data found",Snackbar.LENGTH_SHORT);
                }

            }
        }, (Response.ErrorListener) Throwable::printStackTrace){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name",bundle.getString(Constants.USER_NAME));
                stringMap.put("db_action",bundle.getString("db_action"));
                stringMap.put("total_data",bundle.getString("total_data"));
                stringMap.put("data1",bundle.getString("data1"));
                stringMap.put("data2",bundle.getString("data2"));
                stringMap.put("data3",bundle.getString("data3"));
                stringMap.put("data4",bundle.getString("data4"));
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(personSearchFragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
