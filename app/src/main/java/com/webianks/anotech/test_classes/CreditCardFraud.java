package com.webianks.anotech.test_classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.webianks.anotech.R;

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

    }
}
