package com.webianks.anotech.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by R Ankit on 16-03-2017.
 */

public class AnotechDBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "anotech.db";
    private static final int DB_VERSION = 1;

    public AnotechDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
