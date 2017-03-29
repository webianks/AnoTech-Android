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

import com.webianks.anotech.R;
import com.webianks.anotech.data.TransactionData;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.database.Projections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class CreditCardFraud extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText customerNumberET;
    private TextInputEditText cardNumberET;
    private TextInputEditText paymentDateET;
    private TextInputEditText amountET;
    private String TAG = CreditCardFraud.class.getSimpleName();

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
            gc.set(Calendar.MONTH, Integer.valueOf(splittedPaymentDate[1])-1);
            gc.set(Calendar.YEAR, Integer.valueOf(splittedPaymentDate[0]));

            if (Integer.valueOf(splittedPaymentDate[0]) == 2004) {

                int DAY_OF_YEAR = gc.get(Calendar.DAY_OF_YEAR);
                boolean matched = false;

                Log.d(TAG, cardNumber+" dayofYear: " + DAY_OF_YEAR);

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

        for (int i = 0; i < transactionDataList.size(); i++) {

            Log.d(TAG, "runCheck: " + transactionDataList.get(i).getCount());

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
                customer_number.trim().length() > 0
                ) {


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

        return super.onOptionsItemSelected(item);
    }

}
