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

import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.screens.ResultsActivity;

/**
 * Created by R Ankit on 26-04-2017.
 */

public class WrongTransaction extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText pnrNumberET;
    private TextInputEditText transactionNumberET;
    private TextInputEditText paymentModeET;
    private TextInputEditText amountET;
    private String TAG = CreditCardFraud.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_transactions);

        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pnrNumberET = (TextInputEditText) findViewById(R.id.pnrNumber);
        transactionNumberET = (TextInputEditText) findViewById(R.id.transactionNumber);
        amountET = (TextInputEditText) findViewById(R.id.amount);
        paymentModeET = (TextInputEditText) findViewById(R.id.paymentMode);

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

        Cursor cursor = database.query(Contract.TABLE_TRANSACTIONS, null,
                null, null, null, null, null);

        boolean found = false;
        StringBuilder outlierText = new StringBuilder();

        while (cursor.moveToNext()) {

            int transaction_number_index = cursor.getColumnIndex(Contract.TransactionEntry.TRANSACTION_NUMBER);
            String transactionNumber = cursor.getString(transaction_number_index);

            String selection = Contract.TABLE_TICKETS + "." + Contract.TicketEntry.TRANSACTION_NUMBER + " = ? ";

            String[] selectionArgs = {transactionNumber};
            Cursor cursor_ticket = database.query(Contract.TABLE_TICKETS, null,
                    selection, selectionArgs, null, null, null);

            if (!cursor_ticket.moveToFirst()) {
                Log.d(WrongTransaction.class.getSimpleName(), "Wrong transaction: " + transactionNumber);

                outlierText.append("Wrong transaction: " + transactionNumber + "\n");
                found = true;
            }

            cursor_ticket.close();

        }
        cursor.close();

        if (!found)
            Log.d(TAG, "Data looks good.");
        else {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("type", "wrong_transactions");
            intent.putExtra("reason", getString(R.string.wrong_transactions_reason));
            intent.putExtra("outlier", outlierText.toString());
            startActivity(intent);
        }

    }


    private class DatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... values) {

            runCheck();
            return null;
        }

        protected void onPostExecute(Void result) {
        }
    }


    private void insertNow() {

        String pnr_number = pnrNumberET.getText().toString();
        String amount = amountET.getText().toString();
        String transaction_number = transactionNumberET.getText().toString();
        String payment_mode = paymentModeET.getText().toString();


        if (pnr_number.trim().length() > 0 &&
                amount.trim().length() > 0 &&
                transaction_number.trim().length() > 0 &&
                payment_mode.trim().length() > 0) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.TransactionEntry.PNR_NUMBER, pnr_number);
            contentValues.put(Contract.TransactionEntry.TRANSACTION_NUMBER, transaction_number);
            contentValues.put(Contract.TransactionEntry.MODE, payment_mode);
            contentValues.put(Contract.TransactionEntry.AMOUNT, Integer.valueOf(amount));


            long code = database.insert(Contract.TABLE_TRANSACTIONS, null, contentValues);


            if (code > 0)
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show();
            else
                Log.d(WrongTransaction.class.getSimpleName(), "insertNow: " + code);

            database.close();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.run_check)
            new WrongTransaction.DatabaseTask().execute();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

