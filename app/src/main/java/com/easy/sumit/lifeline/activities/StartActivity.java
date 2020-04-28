package com.easy.sumit.lifeline.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.easy.sumit.lifeline.R;
import com.easy.sumit.lifeline.fragments.AboutUs;
import com.easy.sumit.lifeline.fragments.ContactUs;
import com.easy.sumit.lifeline.fragments.PersonSearchFragment;
import com.easy.sumit.lifeline.fragments.ProfileFragment;
import com.easy.sumit.lifeline.fragments.UserReview;

public class StartActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact) {
            ContactUs contactUs = new ContactUs();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("ContactUs")
                    .replace(R.id.content_home_relative_layout, contactUs).commit();
        } else if (id == R.id.nav_about_us) {
            AboutUs aboutUs = new AboutUs();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack("AboutUs")
                    .replace(R.id.content_home_relative_layout, aboutUs).commit();
        } else if (id == R.id.nav_log_out) {
            SharedPreferences sharedPreferences = getSharedPreferences("lifeline", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            try {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return false;
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){

                case 0:
                    return new ProfileFragment();
                case 1:
                    return new PersonSearchFragment();
                case 2:
                    return new UserReview();
                case 3:

                    break;
                default:
                    Log.e("Tab Error","Unknown Tab Position in MainActivity at line 77");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
