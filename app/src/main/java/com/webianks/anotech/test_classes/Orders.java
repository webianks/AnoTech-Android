package com.webianks.anotech.test_classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.webianks.anotech.FileUtils;
import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.database.Projections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by R Ankit on 24-03-2017.
 */

public class Orders extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText orderNumberET;
    private TextInputEditText orderDateET;
    private TextInputEditText requiredDateET;
    private TextInputEditText shippedDateET;
    private TextInputEditText statusET;
    private TextInputEditText commentsET;
    private TextInputEditText customerNumberET;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.orders);

        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        orderNumberET = (TextInputEditText) findViewById(R.id.orderNumber);
        orderDateET = (TextInputEditText) findViewById(R.id.orderDate);
        requiredDateET = (TextInputEditText) findViewById(R.id.requiredDate);
        shippedDateET = (TextInputEditText) findViewById(R.id.shippedDate);
        statusET = (TextInputEditText) findViewById(R.id.status);
        commentsET = (TextInputEditText) findViewById(R.id.comments);
        customerNumberET = (TextInputEditText) findViewById(R.id.customerNumber);

        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.run_test).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.insert)
            insertNow();
        else
            runCheck();
    }


    @SuppressLint("WrongConstant")
    private void runCheck() {

        AnotechDBHelper dbHelper = new AnotechDBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] selectionArgs = {"10165", ""};

        String selection = Contract.TABLE_ORDERS +
                "." + Contract.OrdersEntry.ORDER_NUMBER + " != ? AND " +
                Contract.TABLE_ORDERS + "." + Contract.OrdersEntry.SHIPPED_DATE + " != ?";

        Cursor cursor = database.query(Contract.TABLE_ORDERS, Projections.ORDERS_COLUMNS,
                selection, selectionArgs, null, null, null);

        StringBuilder stringBuilder = new StringBuilder();
        List<Long> listOfDays = new ArrayList<>();

        while (cursor.moveToNext()) {

            int order_number_index = cursor.getColumnIndex(Contract.OrdersEntry.ORDER_NUMBER);
            int shipped_date_index = cursor.getColumnIndex(Contract.OrdersEntry.SHIPPED_DATE);
            int order_date_index = cursor.getColumnIndex(Contract.OrdersEntry.ORDER_DATE);

            String orderNumber = cursor.getString(order_number_index);
            String orderDate = cursor.getString(order_date_index);
            String shippedDate = cursor.getString(shipped_date_index);

            String[] splittedOrderDate = orderDate.split("-");
            String[] splittedShippedDate = shippedDate.split("-");

            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedOrderDate[2]));
            calender.set(Calendar.MONTH, Integer.valueOf(splittedOrderDate[1]));
            calender.set(Calendar.YEAR, Integer.valueOf(splittedOrderDate[0]));

            Calendar calender2 = Calendar.getInstance();
            calender2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedShippedDate[2]));
            calender2.set(Calendar.MONTH, Integer.valueOf(splittedShippedDate[1]));
            calender2.set(Calendar.YEAR, Integer.valueOf(splittedShippedDate[0]));

            long diff = calender2.getTimeInMillis() - calender.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);
            listOfDays.add(days);

            //Log.d(Orders.class.getSimpleName(),"OrderNumber: "+orderNumber+" -- Days: " + days);

            stringBuilder.append(orderNumber + "," + days + "\n");

        }

        FileUtils.createOutputFile();
        if (FileUtils.writeOutputFile(stringBuilder.toString()))
            Log.d(Orders.class.getSimpleName(), "Writing csv file done.");


        double sd = 0;
        double sum = 0;

        for (int i = 0; i < listOfDays.size(); i++)
            sum = sum + listOfDays.get(i);

        double average = sum / listOfDays.size();

        System.out.println("Average value is : " + average);

        for (int i = 0; i < listOfDays.size(); i++) {
            double difference = listOfDays.get(i) - average;
            sd += (difference * difference) / listOfDays.size();
        }

        double standardDeviation = Math.sqrt(sd);

        System.out.println("Std Devation is: " + standardDeviation);

        int allowedDays = (int) Math.ceil(standardDeviation + average);

        System.out.println("Non anomalous days count: " + allowedDays);


        Log.d(Orders.class.getSimpleName(), " " + cursor.getCount());

    }


    private void insertNow() {

        String order_number = orderNumberET.getText().toString();
        String order_date = orderDateET.getText().toString();
        String required_date = requiredDateET.getText().toString();
        String shipped_date = shippedDateET.getText().toString();
        String status = statusET.getText().toString();
        String comment = commentsET.getText().toString();
        String customer_number = customerNumberET.getText().toString();


        if (order_number.trim().length() > 0 &&
                order_date.trim().length() > 0 &&
                required_date.trim().length() > 0 &&
                shipped_date.trim().length() > 0 &&
                status.trim().length() > 0 &&
                customer_number.trim().length() > 0
                ) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.OrdersEntry.ORDER_NUMBER, Integer.valueOf(order_number));
            contentValues.put(Contract.OrdersEntry.ORDER_DATE, order_date);
            contentValues.put(Contract.OrdersEntry.REQUIRED_DATE, required_date);
            contentValues.put(Contract.OrdersEntry.SHIPPED_DATE, shipped_date);
            contentValues.put(Contract.OrdersEntry.STATUS, status);
            contentValues.put(Contract.OrdersEntry.COMMENTS, comment);
            contentValues.put(Contract.OrdersEntry.CUSTOMER_NUMBER, customer_number);

            long code = database.insert(Contract.TABLE_ORDERS, null, contentValues);


            if (code > 0)
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show();
            else
                Log.d(Orders.class.getSimpleName(), "insertNow: " + code);

            database.close();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


}
