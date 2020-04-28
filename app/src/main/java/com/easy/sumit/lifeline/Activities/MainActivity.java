package com.easy.sumit.lifeline.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easy.sumit.lifeline.Fragments.PersonSearchFragment;
import com.easy.sumit.lifeline.Fragments.ProfileFragment;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.BackgroundWorkers.DataModal.Person;
import com.easy.sumit.lifeline.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Person person;
    private String personName="",user_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        person=new Person();

        Bundle bundle=getIntent().getExtras();
        String last_activity=bundle.getString("last_activity");
        if (last_activity != null) {
            if(last_activity.equals("SplashActivity.java")){
                person.setAllByPreferences(this);
                personName=person.getName();
                user_name=person.getUser_name();
            }
            else if(last_activity.equals("LoginActivity.java")){
                user_name=bundle.getString(Constants.USER_NAME);
                personName=bundle.getString(Constants.NAME);
                setPerson();
            }
        }
        updateFirebaseID();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
    }

    private void updateFirebaseID(){
        final String fid=FirebaseInstanceId.getInstance().getToken();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://10.0.2.2:9090/lifeline_app/fcmnotify.php",
                (Response.Listener<String>) response -> Log.i("**Token Update Responce","responce:"+response),
                (Response.ErrorListener) error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name", ""+user_name);
                stringMap.put("firebaseID", ""+fid);
                stringMap.put("choice", "1");
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        Log.i("***fid***",""+fid);
    }

    public void setPerson(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                "http://10.0.2.2:9090/lifeline_app/getData.php",
                (Response.Listener<String>) response -> {
                    person.setAll(response);
                    person.updatePreferences(MainActivity.this);
                },
                (Response.ErrorListener) error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put("user_name",user_name);
                stringMap.put("db_action","3");
                stringMap.put("total_data","0");
                return stringMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        TextView user_name_view = (TextView) findViewById(R.id.user_name_Nav);
        user_name_view.setText(personName);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            ProfileFragment profileFragment=new ProfileFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home_relative_layout,profileFragment).commit();
        } else if (id == R.id.nav_person) {
            PersonSearchFragment personSearchFragment=new PersonSearchFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home_relative_layout,personSearchFragment).commit();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_contact) {

        }else if(id== R.id.nav_log_out){
            SharedPreferences sharedPreferences=getSharedPreferences("lifeline",MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            try {
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}