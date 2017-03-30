package com.webianks.anotech.test_classes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.webianks.anotech.FileUtils;
import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.screens.ResultsActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class ProductPriceZeroAnomaly extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText orderNumberET;
    private TextInputEditText orderDateET;
    private TextInputEditText requiredDateET;
    private TextInputEditText shippedDateET;
    private TextInputEditText statusET;
    private TextInputEditText commentsET;
    private TextInputEditText customerNumberET;
    private String TAG = ProductPriceZeroAnomaly.class.getSimpleName();
    private ProgressDialog progressDialog;


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


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Anomaly Test");
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.insert)
            insertNow();
    }


    @SuppressLint("WrongConstant")
    private void runCheck() {

        AnotechDBHelper dbHelper = new AnotechDBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(Contract.TABLE_ORDERS, null,
                null, null, null, null, null);

        Map<String, Integer> dateCountMap = new HashMap<>();
        long lastDateMiliSec = 0;
        StringBuilder outlierText = new StringBuilder();

        while (cursor.moveToNext()) {

            int order_date_index = cursor.getColumnIndex(Contract.OrdersEntry.ORDER_DATE);
            String orderDate = cursor.getString(order_date_index);
            String[] splittedOrderDate = orderDate.split("-");

            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedOrderDate[2]));
            calender.set(Calendar.MONTH, Integer.valueOf(splittedOrderDate[1])-1);
            calender.set(Calendar.YEAR, Integer.valueOf(splittedOrderDate[0]));

            long orderDateInMiliseconds = calender.getTimeInMillis();

            if (lastDateMiliSec == orderDateInMiliseconds) {

                dateCountMap.put(orderDate, dateCountMap.get(orderDate) + 1);
                lastDateMiliSec = orderDateInMiliseconds;

            } else {
                lastDateMiliSec = orderDateInMiliseconds;
                //set date and no of orders on that date
                dateCountMap.put(orderDate, 1);
            }

            //Log.d(ProductPriceZeroAnomaly.class.getSimpleName(), "runCheck: " + orderDateInMiliseconds);

        }
        cursor.close();
        database.close();

        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : dateCountMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Log.d(ProductPriceZeroAnomaly.class.getSimpleName(), "Order Date: " + pair.getKey() + " No of orders: " + pair.getValue());
            stringBuilder.append(pair.getKey() + "," + pair.getValue() + "\n");
        }

        String fileName = "orders_count_on_day_"+System.currentTimeMillis()+".csv";
        FileUtils.createOutputFile(fileName);
        if (FileUtils.writeOutputFile(stringBuilder.toString()))
            Log.d(ProductPriceZeroAnomaly.class.getSimpleName(), "Writing csv file done.");

        double sd = 0;
        double sum = 0;


        for (Object o : dateCountMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            int value = (int) pair.getValue();
            sum = sum + (double) value;
        }

        double average = sum / dateCountMap.size();
        Log.d(TAG, "Average value is : " + average);

        for (Object o : dateCountMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            int value = (int) pair.getValue();
            double difference = (double) value - average;
            sd += (difference * difference) / dateCountMap.size();
        }

        double standardDeviation = Math.sqrt(sd);

        Log.d(TAG, "Std Devation is: " + standardDeviation);

        int normalOrdersCount = (int) Math.ceil(standardDeviation + average);

        Log.d(TAG, "Normal order max count: " + normalOrdersCount);

        for (Object o : dateCountMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            int value = (int) pair.getValue();
            if ( value > (normalOrdersCount+1)){
                Log.d(TAG, "Abnormal count of orders on date : " + pair.getKey() + " with orders as : " + pair.getValue());
                outlierText.append("Abnormal count of orders on date : " + pair.getKey() + " with orders as : " + pair.getValue()+"\n");
            }

        }

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("type","product_price");
        intent.putExtra("file",fileName);
        intent.putExtra("reason", getString(R.string.product_price_zero_reason)+normalOrdersCount);
        intent.putExtra("outlier", outlierText.toString());
        startActivity(intent);

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
        else if (item.getItemId() == R.id.run_check)
            new DatabaseTask().execute();

        return super.onOptionsItemSelected(item);
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();

        }

        protected Void doInBackground(Void...values) {

            runCheck();
            return null;
        }

        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



}
