package com.easy.sumit.lifeline.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment{
    Person person = new Person();
    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    FloatingActionButton fab;
    TextView personLastDonated;
    TextView personState;
    TextView personDistrict;
    TextView personSubdistrict;
    TextView personContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView personName = (TextView) getView().findViewById(R.id.personNameView);
        TextView personUserName = (TextView) getView().findViewById(R.id.personUserNameView);
        TextView personMail = (TextView) getView().findViewById(R.id.personEmailView);
        TextView personAddress = (TextView) getView().findViewById(R.id.personAddressView);
        personContact = (TextView) getView().findViewById(R.id.personContactView);
        personLastDonated = (TextView) getView().findViewById(R.id.personLastDonatedView);
        TextView personBloodGroup = (TextView) getView().findViewById(R.id.personBloodGroupView);
        TextView personGender= (TextView) getView().findViewById(R.id.personGenderView);
        TextView personAge= (TextView) getView().findViewById(R.id.personAgeView);
        personState= (TextView) getView().findViewById(R.id.personStateView);
        personDistrict= (TextView) getView().findViewById(R.id.personDistrictView);
        personSubdistrict= (TextView) getView().findViewById(R.id.personSubdistrictView);

        person.setAllByPreferences(getContext());

        personName.setText(person.getName());
        personAge.setText("Age: "+person.getAge());
        personBloodGroup.setText("Blood Group: "+person.getBlood_group());
        personGender.setText("Gender: "+person.getGender());
        personLastDonated.setText("Last Donated: "+person.getLast_donated());
        personMail.setText("Mail: "+person.getUser_mail());
        personContact.setText("Contact No. "+person.getContact_no());
        personAddress.setText("Address: "+person.getAddress());
        personUserName.setText("Username: "+person.getUser_name());
        personState.setText("State: "+person.getState());
        personDistrict.setText("District: "+person.getDistrict());
        personSubdistrict.setText("Sub district: "+person.getSub_district());

        fab = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                View view1=View.inflate(getContext(),R.layout.edit_profile_layout,null);
                alertDialog=new AlertDialog.Builder(getContext());
                alertDialog.setView(view1);
                alertDialog.setCancelable(true);
                dialog=alertDialog.create();
                dialog.show();
                Button changePassword= (Button) view1.findViewById(R.id.changePassword);
                Button changeLastDonated= (Button) view1.findViewById(R.id.changeLastDonated);
                Button changeLocation= (Button) view1.findViewById(R.id.changeLocation);
                Button changeContactNo= (Button) view1.findViewById(R.id.changeContactNo);
                Button changeSecurityQuestion= (Button) view1.findViewById(R.id.changeSecurityQuestion);
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View passwordChangeView=View.inflate(getContext(),R.layout.update_password_layout,null);
                        final Dialog changePasswordDialog=new Dialog(getContext());
                        changePasswordDialog.setContentView(passwordChangeView);
                        final EditText oldPassword= (EditText) changePasswordDialog.findViewById(R.id.old_password);
                        final EditText newPassword= (EditText) changePasswordDialog.findViewById(R.id.new_password);
                        final EditText confPassword= (EditText) changePasswordDialog.findViewById(R.id.conf_password);
                        Button updatePassword= (Button) changePasswordDialog.findViewById(R.id.updatePassword);
                        changePasswordDialog.show();
                        updatePassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!oldPassword.getText().toString().equals("")){
                                    if(!newPassword.getText().toString().equals("")){
                                        if(!confPassword.getText().toString().equals("")){
                                            if(newPassword.getText().toString().equals(""+confPassword.getText().toString())){
                                                if(!(newPassword.getText().toString().length() <5)) {
                                                    String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                                                            changePasswordDialog.cancel();
                                                            dialog.cancel();
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
                                                            stringMap.put("user_name", "" + person.getUser_name());
                                                            stringMap.put("db_action", "5");
                                                            stringMap.put("total_data", "2");
                                                            stringMap.put("old_password", "" + oldPassword.getText().toString());
                                                            stringMap.put("new_password", "" + newPassword.getText().toString());
                                                            return stringMap;
                                                        }
                                                    };
                                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                                    requestQueue.add(stringRequest);
                                                }else{
                                                    Toast.makeText(getContext(),"Password length is short",Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(getContext(),"Passwords not match",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getContext(),"Confirm password",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(),"Enter new password",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getContext(),"Enter old password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                changeLastDonated.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View lastdonatedChangeView = View.inflate(getContext(), R.layout.update_last_donated_layout, null);
                        final Dialog changeLastDonatedDialog = new Dialog(getContext());
                        changeLastDonatedDialog.setContentView(lastdonatedChangeView);
                        final EditText newLastDonated = (EditText) changeLastDonatedDialog.findViewById(R.id.newLastDonated);
                        Button updateLastDonated = (Button) changeLastDonatedDialog.findViewById(R.id.updateLastDonated);
                        changeLastDonatedDialog.show();
                        updateLastDonated.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int dd,mm,yyyy;
                                try {
                                    if(!newLastDonated.getText().equals("")) {

                                        if (newLastDonated.length() > 10) {
                                            Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                        } else {
                                            dd = Integer.parseInt(newLastDonated.getText().toString().substring(0, 2));
                                            mm = Integer.parseInt(newLastDonated.getText().toString().substring(3, 5));
                                            yyyy = Integer.parseInt(newLastDonated.getText().toString().substring(6, 10));
                                            if (mm > 12 || dd > 31 || yyyy > 2018) {
                                                Toast.makeText(getContext(), "Invalid date", Toast.LENGTH_SHORT).show();
                                                Log.i("Date", "DD:" + dd + "MM:" + mm + "YYYY:" + yyyy);
                                                return;
                                            }
                                            if (mm == 2 && dd > 29) {
                                                Toast.makeText(getContext(), "Invalid day in february", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        Log.i("Date",""+newLastDonated.getText().toString());
                                        String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                                                changeLastDonatedDialog.cancel();
                                                dialog.cancel();
                                                person.setLast_donated(newLastDonated.getText().toString());
                                                person.updatePreferences(getContext());
                                                personLastDonated.setText("Last donated: "+person.getLast_donated());
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
                                                stringMap.put("user_name", "" + person.getUser_name());
                                                stringMap.put("db_action", "6");
                                                stringMap.put("total_data", "1");
                                                stringMap.put("last_donated", "" + newLastDonated.getText().toString());
                                                return stringMap;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                        requestQueue.add(stringRequest);
                                    }
                                    else{
                                        Toast.makeText(getContext(), "No date entered", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),"Invalid date",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                });

                changeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateLocationFrgment updateLocationFrgment=UpdateLocationFrgment.newInstance(person,ProfileFragment.this);
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack("UpdateLocationFragment")
                                .replace(R.id.profile_fragment_relative_layout,updateLocationFrgment).commit();
                        fab.setVisibility(View.GONE);
                        dialog.cancel();
                    }
                });
                changeContactNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View contactChangeView = View.inflate(getContext(), R.layout.update_contact_no_layout, null);
                        final Dialog changeContactNoDialog = new Dialog(getContext());
                        changeContactNoDialog.setContentView(contactChangeView);
                        final EditText newContactNo = (EditText) changeContactNoDialog.findViewById(R.id.newContactNo);
                        Button updateContactNo = (Button) changeContactNoDialog.findViewById(R.id.updateContactNo);
                        changeContactNoDialog.show();
                        updateContactNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if(!newContactNo.getText().equals("")) {
                                        Log.i("Contact no: ",""+newContactNo.getText().toString());
                                        String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                                                changeContactNoDialog.cancel();
                                                dialog.cancel();
                                                person.setContact_no(newContactNo.getText().toString());
                                                person.updatePreferences(getContext());
                                                personContact.setText("Contact No. "+person.getContact_no());
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
                                                stringMap.put("user_name", "" + person.getUser_name());
                                                stringMap.put("db_action", "8");
                                                stringMap.put("total_data", "1");
                                                stringMap.put("contact_no", "" + newContactNo.getText().toString());
                                                return stringMap;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                        requestQueue.add(stringRequest);
                                    }
                                    else{
                                        Toast.makeText(getContext(), "No Contact no. entered", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (NumberFormatException e){
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),"Invalid contact no.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                });

                changeSecurityQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View contactChangeView = View.inflate(getContext(), R.layout.update_sec_question_layout, null);
                        final Dialog changeSecurityQuestionDialog = new Dialog(getContext());
                        changeSecurityQuestionDialog.setContentView(contactChangeView);
                        final String[] sec_question = {""};
                        final Spinner spinnerSecurityQ= (Spinner) changeSecurityQuestionDialog.findViewById(R.id.spinnerSecurityQ);
                        ArrayAdapter<CharSequence> arrayAdapterSecQuestion= ArrayAdapter.createFromResource(getContext(),
                                R.array.sec_questions,
                                R.layout.support_simple_spinner_dropdown_item);
                        spinnerSecurityQ.setAdapter(arrayAdapterSecQuestion);
                        spinnerSecurityQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                sec_question[0] =spinnerSecurityQ.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        final EditText q_answer = (EditText) changeSecurityQuestionDialog.findViewById(R.id.q_answer);
                        Button update_button= (Button) changeSecurityQuestionDialog.findViewById(R.id.update_button);
                        changeSecurityQuestionDialog.show();

                        update_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!q_answer.getText().equals("")||!sec_question[0].equals("")) {
                                    Log.i("Security Question: ",""+sec_question[0]);
                                    Log.i("Security Answer: ",""+q_answer.getText().toString());
                                    String url = "http://10.0.2.2:9090/lifeline_app/getData.php";
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                                            changeSecurityQuestionDialog.cancel();
                                            dialog.cancel();
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
                                            stringMap.put("user_name", "" + person.getUser_name());
                                            stringMap.put("db_action", "9");
                                            stringMap.put("total_data", "2");
                                            stringMap.put("sec_question", "" + sec_question[0]);
                                            stringMap.put("sec_answer", "" + q_answer.getText().toString());
                                            return stringMap;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    requestQueue.add(stringRequest);
                                }
                                else{
                                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(person!=null){
            person.setAllByPreferences(getContext());
        }
    }
}
