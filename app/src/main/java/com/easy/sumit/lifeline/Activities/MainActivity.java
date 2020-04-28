package com.easy.sumit.lifeline.Activities;

import android.Manifest;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.easy.sumit.lifeline.Fragments.PersonSearchFragment;
import com.easy.sumit.lifeline.Fragments.ProfileFragment;
import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String personName="",user_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        user_name=bundle.getString(Constants.USER_NAME);
        personName=bundle.getString(Constants.NAME);

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
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,user_name);
            profileFragment.setArguments(bundle);
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home_relative_layout,profileFragment).commit();
        } else if (id == R.id.nav_person) {
            PersonSearchFragment personSearchFragment=new PersonSearchFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.USER_NAME,user_name);
            personSearchFragment.setArguments(bundle);
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home_relative_layout,personSearchFragment).commit();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_contact) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
