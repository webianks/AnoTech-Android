package com.webianks.anotech.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.webianks.anotech.R;

/**
 * Created by R Ankit on 16-03-2017.
 */

public class AnotechDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "anotech.db";
    private static final int DB_VERSION = 44;
    private Context context;

    public AnotechDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create tables
        sqLiteDatabase.execSQL(context.getString(R.string.create_table_customer));
        sqLiteDatabase.execSQL(context.getString(R.string.create_employees));
        sqLiteDatabase.execSQL(context.getString(R.string.create_offices));
        sqLiteDatabase.execSQL(context.getString(R.string.create_order_Details));
        sqLiteDatabase.execSQL(context.getString(R.string.create_orders));
        sqLiteDatabase.execSQL(context.getString(R.string.create_payments));
        sqLiteDatabase.execSQL(context.getString(R.string.create_product_lines));
        sqLiteDatabase.execSQL(context.getString(R.string.create_products));
        sqLiteDatabase.execSQL(context.getString(R.string.create_anomalies));
        sqLiteDatabase.execSQL(context.getString(R.string.create_system_log));

        //insert_into_tables
        sqLiteDatabase.execSQL(context.getString(R.string.insert_customers));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_employees));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_offices));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_order_details));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_order_details_two));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_order_details_three));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_orders));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_payments));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_product_lines));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_products));
        sqLiteDatabase.execSQL(context.getString(R.string.insert_system_log));

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS customers");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS employees");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS offices");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS orderdetails");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS orders");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS payments");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS productlines");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS anomalies");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS system_log");

        onCreate(sqLiteDatabase);
    }
}
