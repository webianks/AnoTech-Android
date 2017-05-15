package com.webianks.anotech.test_classes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.Toast;

import com.webianks.anotech.FileUtils;
import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.screens.ResultsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class ProductPriceZeroAnomaly extends AppCompatActivity implements View.OnClickListener {

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

        Button buyMiBT = (Button) findViewById(R.id.buyMiBand);
        Button buyAppleBT = (Button) findViewById(R.id.buyApple);

        buyAppleBT.setOnClickListener(this);
        buyMiBT.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Anomaly Test");
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buyApple)
            buyApple();
        if (view.getId() == R.id.buyMiBand)
            buyMiBand();
    }

    private void buyMiBand() {

        new AlertDialog.Builder(this)
                .setTitle("Buy")
                .setMessage("Are you sure you want to buy this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                        insertNow("QWEBDF", "1999", "Xiomi Mi Band 2");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void buyApple() {

        new AlertDialog.Builder(this)
                .setTitle("Buy")
                .setMessage("Are you sure you want to buy this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue
                        insertNow("ADCSCS", "0", "Apple iPhone 7");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
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
            calender.set(Calendar.MONTH, Integer.valueOf(splittedOrderDate[1]) - 1);
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

        String fileName = "orders_count_on_day_" + System.currentTimeMillis() + ".csv";
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
            if (value > (normalOrdersCount + 1)) {
                Log.d(TAG, "Abnormal count of orders on date : " + pair.getKey() + " with orders as : " + pair.getValue());
                outlierText.append("Abnormal count of orders on date : " + pair.getKey() + " with orders as : " + pair.getValue() + "\n");
            }

        }

        SQLiteDatabase databaseWritable = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.AnomalyEntry.TYPE, "product_price");
        contentValues.put(Contract.AnomalyEntry.FILE, fileName);
        contentValues.put(Contract.AnomalyEntry.REASON, getString(R.string.product_price_zero_reason) + normalOrdersCount);
        contentValues.put(Contract.AnomalyEntry.OUTLIER, outlierText.toString());

        long code = databaseWritable.insert(Contract.TABLE_ANOMALY, null, contentValues);

    }


    private void insertNow(String product_code, String price_each, String comment) {

        String shipped_date = " ";
        String status = "in process";

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String order_date = df.format(c.getTime());

        c.add(Calendar.DAY_OF_YEAR, 7);

        String required_date = df.format(c.getTime());

        if (order_date.trim().length() > 0 &&
                required_date.trim().length() > 0 &&
                status.trim().length() > 0
                ) {

            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            Cursor cursor = database.query(Contract.TABLE_ORDERS, null, null, null, null, null, null);

            String order_number = "1";
            String customer_number = "1";

            if (cursor.moveToLast()) {

                order_number = String.valueOf(cursor.getInt(cursor.getColumnIndex(Contract.OrdersEntry.ORDER_NUMBER)) + 1);
                customer_number = String.valueOf(cursor.getInt(cursor.getColumnIndex(Contract.OrdersEntry.CUSTOMER_NUMBER)) + 1);
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.OrdersEntry.ORDER_DATE, order_date);
            contentValues.put(Contract.OrdersEntry.REQUIRED_DATE, required_date);
            contentValues.put(Contract.OrdersEntry.SHIPPED_DATE, shipped_date);
            contentValues.put(Contract.OrdersEntry.STATUS, status);
            contentValues.put(Contract.OrdersEntry.COMMENTS, comment);
            contentValues.put(Contract.OrdersEntry.ORDER_NUMBER, order_number);
            contentValues.put(Contract.OrdersEntry.CUSTOMER_NUMBER, customer_number);

            long code = database.insert(Contract.TABLE_ORDERS, null, contentValues);

            if (code > 0) {


                String quantity_ordered = "1";
                int order_line_number = 1 + (int) (Math.random() * 10);

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(Contract.OrderDetailsEntry.ORDER_NUMBER, order_number);
                contentValues2.put(Contract.OrderDetailsEntry.PRODUCT_CODE, product_code);
                contentValues2.put(Contract.OrderDetailsEntry.QUANTITY_ORDERED, quantity_ordered);
                contentValues2.put(Contract.OrderDetailsEntry.PRICE_EACH, price_each);
                contentValues2.put(Contract.OrderDetailsEntry.ORDER_LINE_NUMBER, String.valueOf(order_line_number));


                long code2 = database.insert(Contract.TABLE_ORDER_DETAILS, null, contentValues2);

                if (code2 > 0){

                    Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show();
                    new DatabaseTask().execute();

                }

            } else {
                Log.d(ProductPriceZeroAnomaly.class.getSimpleName(), "insertNow: " + code);

            }

            database.close();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();

        }

        protected Void doInBackground(Void... values) {

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
