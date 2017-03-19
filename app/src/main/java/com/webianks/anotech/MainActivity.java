package com.webianks.anotech;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.webianks.anotech.database.AnotechDBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AnotechDBHelper dbHelper = new AnotechDBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();


        String query = "CREATE INDEX salesRepEmployeeNumber ON customers(salesRepEmployeeNumber)";

        //sqLiteDatabase.execSQL(query);

    }
}
