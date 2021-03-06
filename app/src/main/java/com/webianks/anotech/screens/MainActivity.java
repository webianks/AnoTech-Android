package com.webianks.anotech.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.webianks.anotech.R;
import com.webianks.anotech.adapters.ViewPagerAdapter;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.fragments.AnomalousActivities;
import com.webianks.anotech.fragments.Testing;
import com.webianks.anotech.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fillDatabase();

        StringBuilder allLogs = LogUtils.readLogs();
        String result = allLogs.substring(allLogs.indexOf("Open ") + 4, allLogs.indexOf(" in your browser"));
        Log.d("webi",result);

        SharedPreferences sharedPref = getSharedPreferences("ANO_PREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.key_db_link), result);
        editor.apply();

    }

    private void fillDatabase() {
        AnotechDBHelper anotechDBHelper = new AnotechDBHelper(this);
        SQLiteDatabase database = anotechDBHelper.getWritableDatabase();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Testing(), "Testing");
        //adapter.addFragment(new AnomalousActivities(), "Activities");
        adapter.addFragment(new AnomalousActivities(), "Anomalies");
        viewPager.setAdapter(adapter);
    }
}
