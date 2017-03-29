package com.webianks.anotech.test_classes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
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

import com.webianks.anotech.R;
import com.webianks.anotech.data.TransactionData;
import com.webianks.anotech.data.TransactionInMonth;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.database.Projections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class CreditCardFraud extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText customerNumberET;
    private TextInputEditText cardNumberET;
    private TextInputEditText paymentDateET;
    private TextInputEditText amountET;
    private String TAG = CreditCardFraud.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_anomaly);

        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        customerNumberET = (TextInputEditText) findViewById(R.id.customerNumber);
        cardNumberET = (TextInputEditText) findViewById(R.id.cardNumber);
        paymentDateET = (TextInputEditText) findViewById(R.id.paymentDate);
        amountET = (TextInputEditText) findViewById(R.id.amount);

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

        Cursor cursor = database.query(Contract.TABLE_PAYMENTS, null,
                null, null, null, null, null);

        List<TransactionData> transactionDataList = new ArrayList<>();

        while (cursor.moveToNext()) {

            int card_number_index = cursor.getColumnIndex(Contract.PaymentsEntry.CARD_NUMBER);
            int payment_date_index = cursor.getColumnIndex(Contract.PaymentsEntry.PAYMENT_DATE);


            String cardNumber = cursor.getString(card_number_index);
            String paymentDate = cursor.getString(payment_date_index);


            String[] splittedPaymentDate = paymentDate.split("-");

            GregorianCalendar gc = new GregorianCalendar();
            gc.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedPaymentDate[2]));
            gc.set(Calendar.MONTH, Integer.valueOf(splittedPaymentDate[1]) - 1);
            gc.set(Calendar.YEAR, Integer.valueOf(splittedPaymentDate[0]));

            if (Integer.valueOf(splittedPaymentDate[0]) == 2004) {

                int DAY_OF_YEAR = gc.get(Calendar.DAY_OF_YEAR);
                boolean matched = false;

                Log.d(TAG, cardNumber + " dayofYear: " + DAY_OF_YEAR);

                for (int i = 0; i < transactionDataList.size(); i++) {

                    if (transactionDataList.get(i).getCardNumber().equals(cardNumber)) {

                        transactionDataList.get(i).setDayNumber(DAY_OF_YEAR);
                        matched = true;
                    }

                }

                if (!matched) {

                    TransactionData transactionData = new TransactionData();
                    transactionData.setCardNumber(cardNumber);
                    transactionData.setDayNumber(DAY_OF_YEAR);
                    transactionDataList.add(transactionData);

                }

            }

        }

        cursor.close();

        List<TransactionInMonth> transactionInMonthsList = new ArrayList<>();

        for (int i = 0; i < transactionDataList.size(); i++) {

            TransactionInMonth transactionInMonth = new TransactionInMonth();
            transactionInMonth.setCardNumber(transactionDataList.get(i).getCardNumber());

            int counts[] = new int[368];
            counts[0] = 0;

            for (int j = 1; j <= 367; j++) {
                int[] mainCounts = transactionDataList.get(i).getCount();
                counts[j] = counts[j - 1] + mainCounts[j - 1];
            }

            int start = 0;
            for (int k = 1; k <= 12; k++) {

                int monthSum = counts[k * 30] - counts[start];
                transactionInMonth.setCount(monthSum);
                start = k*30;
                Log.d(TAG, "Sum: " + transactionDataList.get(i).getCardNumber() + " " + monthSum);

            }

            transactionInMonthsList.add(transactionInMonth);

        }

        performMeanCheck(transactionInMonthsList);

    }


    private void performMeanCheck(List<TransactionInMonth> transactionInMonthsList) {


        for (int i = 0; i < transactionInMonthsList.size(); i++) {

            Log.d(TAG,"Final occurance values "+transactionInMonthsList.get(i).getCardNumber()+" "+
                    transactionInMonthsList.get(i).getCount().toString());

            double sd = 0;
            double sum = 0;

            for (int j = 0; j< transactionInMonthsList.get(i).getCount().size();j++) {
                int value = transactionInMonthsList.get(i).getCount().get(j);
                sum = sum + (double) value;
            }

            double average = sum / transactionInMonthsList.get(i).getCount().size();
            Log.d(TAG, "Average value is : " + average);



            for (int j = 0; j<transactionInMonthsList.get(i).getCount().size();j++) {
                int value = transactionInMonthsList.get(i).getCount().get(j);
                double difference = (double) value - average;
                sd += (difference * difference) / transactionInMonthsList.get(i).getCount().size();
            }

            double standardDeviation = Math.sqrt(sd);

            Log.d(TAG, "Std Deviation is: " + standardDeviation);

            int normalCount = (int) Math.ceil(standardDeviation + average);

            Log.d(TAG, "Normal payment max count: " + normalCount);

            for (int j = 0; j<transactionInMonthsList.get(i).getCount().size();j++) {

                int value = transactionInMonthsList.get(i).getCount().get(j);
                if ( value > normalCount)
                    Log.d(TAG, "Abnormal count of payments by card : " + transactionInMonthsList.get(i).getCardNumber()
                            + " with payments as : " + transactionInMonthsList.get(i).getCount().get(j));
            }
        }

        progressDialog.dismiss();

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


    private void insertNow() {

        String card_number = cardNumberET.getText().toString();
        String amount = amountET.getText().toString();
        String payment_date = paymentDateET.getText().toString();
        String customer_number = customerNumberET.getText().toString();


        if (card_number.trim().length() > 0 &&
                amount.trim().length() > 0 &&
                payment_date.trim().length() > 0 &&
                customer_number.trim().length() > 0) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.PaymentsEntry.CUSTOMER_NUMBER, Integer.valueOf(customer_number));
            contentValues.put(Contract.PaymentsEntry.CARD_NUMBER, Integer.valueOf(card_number));
            contentValues.put(Contract.PaymentsEntry.PAYMENT_DATE, payment_date);
            contentValues.put(Contract.PaymentsEntry.AMOUNT, Integer.valueOf(amount));


            long code = database.insert(Contract.TABLE_PAYMENTS, null, contentValues);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
