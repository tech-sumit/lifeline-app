
package com.easy.sumit.lifeline.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.easy.sumit.lifeline.datamodal.ReviewAdapter;
import com.easy.sumit.lifeline.datamodal.ReviewModel;
import com.easy.sumit.lifeline.datamodal.URLList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserReview extends Fragment {

    private ArrayList<ReviewModel> arrayList = new ArrayList<>();
    private String output="";
    private String user_name="";
    ReviewAdapter adapter;
    ListView listResultView;
    View view;
    public UserReview(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Person person=new Person();
        person.setAllByPreferences(getContext());
        user_name=person.getUser_name();
        Log.e("USER_NAME",""+user_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_review, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        listResultView = (ListView) view.findViewById(R.id.reviewList);
        setListAdapter();
        listResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewModel reviewModel= arrayList.get(position);
                String from_user_name=reviewModel.getFrom_user_name();
                String date=reviewModel.getDate();
                String time=reviewModel.getTime();
                Log.i("***Item Selected***", from_user_name);
                reviewHandler(from_user_name,date,time);
            }
        });
        Log.i("**ResultFragment", "List adapter initialised");
    }
    private void setListAdapter(){
        String call_log_url = URLList.getUrl(getContext(),"call_log");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, call_log_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    if (!response.equals("null")) {
                        if(!response.equals("")) {
                            output = response;
                            Log.i("OUTPUT", "" + output);
                            try {
                                JSONArray jsonArray = new JSONArray(output);
                                JSONObject json;
                                arrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    json = jsonArray.getJSONObject(i);
                                    ReviewModel data = new ReviewModel();

                                    data.setFrom_user("Name: " + json.getString("from_user"));
                                    data.setFrom_user_name("Username: " + json.getString("from_user_name"));
                                    data.setDate("Date: " + json.getString("date"));
                                    data.setTime("Time: " + json.getString("time"));
                                    arrayList.add(data);
                                }
                                adapter = new ReviewAdapter(UserReview.this, arrayList);
                                for (int i = 0; i < arrayList.size(); i++) {
                                    Log.i("***Data***", arrayList.get(i).toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listResultView.setAdapter(adapter);
                        }else {
                            Toast.makeText(view.getContext(), "No reviews available", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    }else {
                        Toast.makeText(view.getContext(), "No reviews available", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                }else {
                    Toast.makeText(view.getContext(), "No reviews available", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name",user_name);
                stringMap.put("choice","4");
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void reviewHandler(final String from_user_name,final String date,final String time){
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        View reviewView=View.inflate(view.getContext(),R.layout.review_layout,null);
        builder.setView(reviewView);
        builder.setCancelable(true);
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        final LinearLayout notiLayout1= (LinearLayout) alertDialog.findViewById(R.id.notiLayout1);
        final LinearLayout notiLayout2= (LinearLayout) alertDialog.findViewById(R.id.notiLayout2);
        Button notiYES= (Button) alertDialog.findViewById(R.id.notiYES);
        Button notiNO= (Button) alertDialog.findViewById(R.id.notiNO);
        Button notiClose= (Button) alertDialog.findViewById(R.id.notiClose);
        Button notiReport= (Button) alertDialog.findViewById(R.id.notiReport);
        Button notiCancel= (Button) alertDialog.findViewById(R.id.notiCancel);
        final EditText notiText= (EditText) alertDialog.findViewById(R.id.notiText);

        final String call_log_url = URLList.getUrl(getContext(),"call_log");

        assert notiYES != null;
        notiYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringRequest stringRequest=new StringRequest(Request.Method.POST, call_log_url , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(getContext()!=null) {
                            Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                        }
                        setListAdapter();
                        alertDialog.cancel();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> stringMap=new HashMap<>();
                        stringMap.put("user_name",""+user_name);
                        stringMap.put("choice","2");
                        stringMap.put("status","true");
                        stringMap.put("from_user_name",""+from_user_name.substring(10));
                        stringMap.put("date",""+date.substring(6));
                        stringMap.put("time",""+time.substring(6));
                        return stringMap;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        assert notiNO != null;
        notiNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert notiLayout1 != null;
                notiLayout1.setVisibility(View.GONE);
                assert notiLayout2 != null;
                notiLayout2.setVisibility(View.VISIBLE);
            }
        });
        assert notiClose != null;
        notiClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert notiReport != null;
        notiReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert notiText != null;
                if(!notiText.getText().toString().equals("")){
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL,"17.sumitagrawal@gmail.com");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Lifeline User Report by, "+user_name);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_TEXT,""+notiText.getText().toString());
                    try{
                        final StringRequest stringRequest=new StringRequest(Request.Method.POST, call_log_url , new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(getContext()!=null) {
                                    Toast.makeText(view.getContext(), ""+response, Toast.LENGTH_SHORT).show();
                                }
                                setListAdapter();
                                alertDialog.cancel();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> stringMap=new HashMap<>();
                                stringMap.put("user_name",""+user_name);
                                stringMap.put("choice","3");
                                stringMap.put("status","true");
                                stringMap.put("from_user_name",""+from_user_name.substring(10));
                                stringMap.put("date",""+date.substring(6));
                                stringMap.put("time",""+time.substring(6));
                                return stringMap;
                            }
                        };
                        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);
                        startActivity(intent);
                    }catch (ActivityNotFoundException exception){
                        exception.printStackTrace();
                        Toast.makeText(view.getContext(),"Email clients not found",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(view.getContext(),"Empty report",Toast.LENGTH_SHORT).show();
                }
            }
        });
        assert notiCancel != null;
        notiCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
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
