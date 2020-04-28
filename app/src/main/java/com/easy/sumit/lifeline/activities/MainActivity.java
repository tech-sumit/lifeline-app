package com.easy.sumit.lifeline.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
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
import com.easy.sumit.lifeline.fragments.AboutUs;
import com.easy.sumit.lifeline.fragments.ContactUs;
import com.easy.sumit.lifeline.fragments.PersonSearchFragment;
import com.easy.sumit.lifeline.fragments.ProfileFragment;
import com.easy.sumit.lifeline.fragments.UserReview;
import com.easy.sumit.lifeline.datamodal.Person;
import com.easy.sumit.lifeline.datamodal.URLList;
import com.easy.sumit.lifeline.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Person person=new Person();
    private String personName="",user_name="";
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        String last_activity=bundle.getString("last_activity");
        if (last_activity != null) {
            switch (last_activity) {
                case "SplashActivity.java":
                    person.setAllByPreferences(this);
                    personName = person.getName();
                    user_name = person.getUser_name();
                    break;
                case "LoginActivity.java":
                    user_name = bundle.getString(Constants.USER_NAME);
                    personName = bundle.getString(Constants.NAME);
                    setPerson();
                    break;
                case "MainActivity.java":
                    person.setAllByPreferences(this);
                    personName = person.getName();
                    user_name = person.getUser_name();
                    break;
            }
        }
        webView= (WebView) findViewById(R.id.webView);
        webView.loadUrl(URLList.getUrl(this,"webpage"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setPerson(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                URLList.getUrl(this,"getData"), new Response.Listener<String>() {
                @Override
            public void onResponse(String response) {
                person.setAll(response);
                person.updatePreferences(MainActivity.this);
                Log.e("******Person Data******",""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_LONG).show();
            }
        }){
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

        if (id == R.id.nav_home){
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("last_activity","MainActivity.java");
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            if(webView!=null){
                webView.destroy();
            }
            ProfileFragment profileFragment=new ProfileFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("ProfileFragment")
                    .replace(R.id.content_home_relative_layout,profileFragment).commit();
        } else if (id == R.id.nav_person) {
            if(webView!=null){
                webView.destroy();
            }
            PersonSearchFragment personSearchFragment=new PersonSearchFragment();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("PersonSearchFragment")
                    .replace(R.id.content_home_relative_layout,personSearchFragment).commit();
        } else if (id == R.id.nav_review) {
            UserReview userReview=new UserReview();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("UserReview")
                    .replace(R.id.content_home_relative_layout,userReview).commit();
        } else if (id == R.id.nav_contact) {
            ContactUs contactUs=new ContactUs();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("ContactUs")
                    .replace(R.id.content_home_relative_layout,contactUs).commit();
        } else if (id == R.id.nav_about_us) {
            AboutUs aboutUs=new AboutUs();
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("AboutUs")
                    .replace(R.id.content_home_relative_layout,aboutUs).commit();
        } else if (id== R.id.nav_log_out){
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.NAME,person.getName());
        bundle.putString(Constants.USER_NAME,person.getUser_name());
        outState.putAll(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(person!=null){
            person.setName(savedInstanceState.getString(Constants.NAME));
            person.setUser_name(savedInstanceState.getString(Constants.USER_NAME));
        }
    }
}
