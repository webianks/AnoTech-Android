package com.webianks.anotech.test_classes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.webianks.anotech.R;
import com.webianks.anotech.data.TransactionData;
import com.webianks.anotech.data.TransactionInMonth;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class CreditCardFraud extends AppCompatActivity implements View.OnClickListener {


    private String TAG = CreditCardFraud.class.getSimpleName();
    private ProgressDialog progressDialog;
    private boolean found;
    private StringBuilder outlierText;
    private TextView productNameTV;
    private TextView billTV;
    private String[] productNames = {"iPhone 7", "Mi Band 2", "Cool Chino", "White Tie", "XBox 360", "Sandisk SD"};
    private String[] bill = {"53000", "1999", "799", "150", "20000", "1700"};
    private EditText cardNumberET;
    private Button insertBT;
    private int number;

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

        billTV = (TextView) findViewById(R.id.bill);
        productNameTV = (TextView) findViewById(R.id.product_name);
        cardNumberET = (EditText) findViewById(R.id.card_number);
        insertBT = (Button) findViewById(R.id.insert);

        insertBT.setOnClickListener(this);


        number = 1 + (int) (Math.random() * productNames.length);

        productNameTV.setText(productNames[number - 1]);
        billTV.setText(bill[number - 1]);

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

        outlierText = new StringBuilder();
        found = false;
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
                start = k * 30;
                Log.d(TAG, "Sum: " + transactionDataList.get(i).getCardNumber() + " " + monthSum);

            }

            transactionInMonthsList.add(transactionInMonth);

        }

        performMeanCheck(transactionInMonthsList);

    }


    private void performMeanCheck(List<TransactionInMonth> transactionInMonthsList) {


        int normalCount = 0;

        for (int i = 0; i < transactionInMonthsList.size(); i++) {

            Log.d(TAG, "Final occurance values " + transactionInMonthsList.get(i).getCardNumber() + " " +
                    transactionInMonthsList.get(i).getCount().toString());

            double sd = 0;
            double sum = 0;

            for (int j = 0; j < transactionInMonthsList.get(i).getCount().size(); j++) {
                int value = transactionInMonthsList.get(i).getCount().get(j);
                sum = sum + (double) value;
            }

            double average = sum / transactionInMonthsList.get(i).getCount().size();
            Log.d(TAG, "Average value is : " + average);


            for (int j = 0; j < transactionInMonthsList.get(i).getCount().size(); j++) {
                int value = transactionInMonthsList.get(i).getCount().get(j);
                double difference = (double) value - average;
                sd += (difference * difference) / transactionInMonthsList.get(i).getCount().size();
            }

            double standardDeviation = Math.sqrt(sd);

            Log.d(TAG, "Std Deviation is: " + standardDeviation);

            normalCount = (int) Math.ceil(standardDeviation + average);

            Log.d(TAG, "Normal payment max count: " + normalCount);

            for (int j = 0; j < transactionInMonthsList.get(i).getCount().size(); j++) {

                int value = transactionInMonthsList.get(i).getCount().get(j);
                if (value > normalCount) {

                    found = true;
                    Log.d(TAG, "Abnormal count of payments by card : " + transactionInMonthsList.get(i).getCardNumber()
                            + " with payments as : " + transactionInMonthsList.get(i).getCount().get(j));

                    outlierText.append("Abnormal count of payments by card : " + transactionInMonthsList.get(i).getCardNumber()
                            + " with payments as : " + transactionInMonthsList.get(i).getCount().get(j) + "\n");
                }
            }

        }

        progressDialog.dismiss();

        if (found) {

            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase databaseWritable = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.AnomalyEntry.TYPE, "credit_card");
            contentValues.put(Contract.AnomalyEntry.FILE, "none");
            contentValues.put(Contract.AnomalyEntry.REASON, getString(R.string.credit_fraud_reason) + normalCount);
            contentValues.put(Contract.AnomalyEntry.OUTLIER, outlierText.toString());

            long code = databaseWritable.insert(Contract.TABLE_ANOMALY, null, contentValues);

        }

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


    private void insertNow() {

        String card_numberET = cardNumberET.getText().toString();
        String card_number;

        if (card_numberET.trim().length() == 13) {
            card_number = card_numberET.substring(card_numberET.length() - 5);
        } else {
            Toast.makeText(this, "Please enter valid card number", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String payment_date = df.format(c.getTime());

        if (card_number.trim().length() > 0) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            Cursor cursor = database.query(Contract.TABLE_PAYMENTS, null, null, null, null, null, null);

            String customer_number = "1";

            if (cursor.moveToLast()) {
                customer_number = String.valueOf(cursor.getInt(cursor.getColumnIndex(Contract.PaymentsEntry.CUSTOMER_NUMBER)) + 1);
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.PaymentsEntry.CUSTOMER_NUMBER, Integer.valueOf(customer_number));
            contentValues.put(Contract.PaymentsEntry.CARD_NUMBER, Integer.valueOf(card_number));
            contentValues.put(Contract.PaymentsEntry.PAYMENT_DATE, payment_date);
            contentValues.put(Contract.PaymentsEntry.AMOUNT, bill[number - 1]);

            long code = database.insert(Contract.TABLE_PAYMENTS, null, contentValues);


            if (code > 0) {
                new DatabaseTask().execute();
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show();
            } else
                Log.d(CreditCardFraud.class.getSimpleName(), "insertNow: " + code);

            database.close();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
