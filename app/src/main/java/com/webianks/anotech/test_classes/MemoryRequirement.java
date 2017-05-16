package com.webianks.anotech.test_classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;

/**
 * Created by R Ankit on 16-05-2017.
 */

public class MemoryRequirement extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_requirement);

        init();
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

    public void onClick(View view) {


        AnotechDBHelper dbHelper = new AnotechDBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(Contract.TABLE_SYSTEM_LOG, null,
                null, null, null, null, null);


        boolean found = false;
        StringBuilder outlierText = new StringBuilder();

        int count = 0;
        while (cursor.moveToNext()) {

            int time_index = cursor.getColumnIndex(Contract.SystemEntry.TIME);
            int memory_used_index = cursor.getColumnIndex(Contract.SystemEntry.MEMORY_USED);


            String time = cursor.getString(time_index);
            float memory = Float.parseFloat(cursor.getString(memory_used_index));

            if (memory >= 85)
                count++;

            if (count == 5){
                count = 0;
                Log.d("webi", "We may require additional memory: " + time);
                outlierText.append("We may require additional memory at time: " + time+"\n");
                found = true;
            }

        }

        if (!found)
            Toast.makeText(this,"No additional memory is needed.",Toast.LENGTH_SHORT).show();
        else{

            Toast.makeText(this,"Analysis done. You can find the results in Anomalies Tab.",Toast.LENGTH_LONG).show();

            SQLiteDatabase databaseWritable = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.AnomalyEntry.TYPE, "extra_memory_needed");
            contentValues.put(Contract.AnomalyEntry.FILE, " ");
            contentValues.put(Contract.AnomalyEntry.REASON, getString(R.string.memory_requirement));
            contentValues.put(Contract.AnomalyEntry.OUTLIER, outlierText.toString());

            String selection = Contract.TABLE_ANOMALY + "." + Contract.AnomalyEntry.TYPE + " = ?";
            String[] selectionArgs = new String[]{"extra_memory_needed"};

            Cursor cursorNew = databaseWritable.query(Contract.TABLE_ANOMALY, null, selection, selectionArgs, null, null, null);

            if (cursorNew.moveToFirst())
                databaseWritable.update(Contract.TABLE_ANOMALY, contentValues, selection, selectionArgs);
            else
                databaseWritable.insert(Contract.TABLE_ANOMALY, null, contentValues);

        }
    }

}
