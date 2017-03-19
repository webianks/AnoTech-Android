package com.webianks.anotech.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by R Ankit on 16-03-2017.
 */

public class AnotechDBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "anotech.db";
    private static final int DB_VERSION = 5;

    public AnotechDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "\n" +
                "CREATE TABLE customers (\n" +
                "   customerNumber int(11) NOT NULL,\n" +
                "   customerName varchar(50) NOT NULL,\n" +
                "   contactLastName varchar(50) NOT NULL,\n" +
                "   contactFirstName varchar(50) NOT NULL,\n" +
                "   phone varchar(50) NOT NULL,\n" +
                "   addressLine1 varchar(50) NOT NULL,\n" +
                "   addressLine2 varchar(50) DEFAULT NULL,\n" +
                "   city varchar(50) NOT NULL,\n" +
                "   state varchar(50) DEFAULT NULL,\n" +
                "   postalCode varchar(15) DEFAULT NULL,\n" +
                "   country varchar(50) NOT NULL,\n" +
                "   salesRepEmployeeNumber int(11) DEFAULT NULL,\n" +
                "   creditLimit decimal(10,2) DEFAULT NULL,\n" +
                "   PRIMARY KEY (customerNumber),\n" +
                "   CONSTRAINT customers_ibfk_1 FOREIGN KEY (salesRepEmployeeNumber) REFERENCES employees (employeeNumber)\n" +
                ");";

        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS customers");
        onCreate(sqLiteDatabase);
    }
}
