package com.webianks.anotech.test_classes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;

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
        //else
        //runCheck();
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
                comment.trim().length() > 0 &&
                customer_number.trim().length() > 0
                ) {


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

        }

    }
}
