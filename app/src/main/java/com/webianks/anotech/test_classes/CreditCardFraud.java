package com.webianks.anotech.test_classes;

import android.content.ContentValues;
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
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;

/**
 * Created by R Ankit on 27-03-2017.
 */

public class CreditCardFraud extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText customerNumberET;
    private TextInputEditText cardNumberET;
    private TextInputEditText paymentDateET;
    private TextInputEditText amountET;

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

    private void runCheck() {
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
