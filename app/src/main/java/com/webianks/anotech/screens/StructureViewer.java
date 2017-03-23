package com.webianks.anotech.screens;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.database.Projections;

/**
 * Created by R Ankit on 22-03-2017.
 */

public class StructureViewer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.structure_viewer);

        init();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getColumns();

    }

    private void getColumns() {

        AnotechDBHelper anotechDBHelper = new AnotechDBHelper(this);
        SQLiteDatabase sqLiteDatabase = anotechDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Contract.TABLE_ORDER_DETAILS, Projections.ORDER_DETAILS_COLUMNS,
                null, null, null, null, null);

        int count = 0;
        if (cursor != null)
            count = cursor.getCount();

        //Toast.makeText(this, "Number of columns " + cursor.getCount(), Toast.LENGTH_LONG).show();

    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}