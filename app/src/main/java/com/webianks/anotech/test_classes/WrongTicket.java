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

public class WrongTicket extends AppCompatActivity implements View.OnClickListener {


    private TextInputEditText ticketNumberET;
    private TextInputEditText seatNumberET;
    private TextInputEditText audiET;
    private TextInputEditText movieET;
    private String TAG = WrongTransaction.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_ticket);

        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ticketNumberET = (TextInputEditText) findViewById(R.id.ticketNumber);
        seatNumberET = (TextInputEditText) findViewById(R.id.seatNumber);
        audiET = (TextInputEditText) findViewById(R.id.audi);
        movieET = (TextInputEditText) findViewById(R.id.movie);

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

        Cursor cursor = database.query(Contract.TABLE_MOVIE_TICKETS, null,
                null, null, null, null, null);

        boolean found = false;
        StringBuilder outlierText = new StringBuilder();

        while (cursor.moveToNext()) {

            int ticket_number_index = cursor.getColumnIndex(Contract.MovieTicketEntry.TICKET_NUMBER);
            String ticketNumber = cursor.getString(ticket_number_index);

            String selection = Contract.TABLE_MOIVE_TRANSACTIONS + "." + Contract.MovieTransactionEntry.TICKET_NUMBER + " = ? ";

            String[] selectionArgs = {ticketNumber};
            Cursor cursor_ticket = database.query(Contract.TABLE_MOIVE_TRANSACTIONS, null,
                    selection, selectionArgs, null, null, null);

            if (!cursor_ticket.moveToFirst()) {
                Log.d(WrongTicket.class.getSimpleName(), "Wrong ticket: " + ticketNumber);

                outlierText.append("Wrong transaction: " + ticketNumber + "\n");
                found = true;
            }

            cursor_ticket.close();

        }
        cursor.close();

        if (!found)
            Toast.makeText(this, "Data looks good.", Toast.LENGTH_LONG).show();
        else {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("type", "wrong_transactions");
            intent.putExtra("reason", getString(R.string.wrong_ticket_reason));
            intent.putExtra("outlier", outlierText.toString());
            startActivity(intent);
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

        String ticket_number = ticketNumberET.getText().toString();
        String seat = seatNumberET.getText().toString();
        String movie = movieET.getText().toString();
        String audi = audiET.getText().toString();


        if (ticket_number.trim().length() > 0 &&
                seat.trim().length() > 0 &&
                movie.trim().length() > 0 &&
                audi.trim().length() > 0) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.MovieTicketEntry.TICKET_NUMBER, ticket_number);
            contentValues.put(Contract.MovieTicketEntry.SEAT_NUMBER, seat);
            contentValues.put(Contract.MovieTicketEntry.MOVIE, movie);
            contentValues.put(Contract.MovieTicketEntry.AUDI, audi);


            long code = database.insert(Contract.TABLE_MOVIE_TICKETS, null, contentValues);


            if (code > 0)
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_LONG).show();
            else
                Log.d(WrongTicket.class.getSimpleName(), "insertNow: " + code);

            database.close();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        else if (item.getItemId() == R.id.run_check)
            new WrongTicket.DatabaseTask().execute();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

