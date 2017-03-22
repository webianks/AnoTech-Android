package com.webianks.anotech.screens;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.webianks.anotech.R;
import com.webianks.anotech.adapters.ViewPagerAdapter;
import com.webianks.anotech.fragments.AnomalousActivities;
import com.webianks.anotech.fragments.DataStructure;
import com.webianks.anotech.fragments.Testing;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DataStructure(), "Structure");
        adapter.addFragment(new AnomalousActivities(), "Activities");
        adapter.addFragment(new Testing(), "Testing");
        viewPager.setAdapter(adapter);
    }
}
