package com.webianks.anotech.test_classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.webianks.anotech.R;

/**
 * Created by R Ankit on 22-03-2017.
 */

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText orderNumnberET;
    private TextInputEditText productCodeET;
    private TextInputEditText quantityOrderedET;
    private TextInputEditText priceEachET;
    private TextInputEditText orderLineNumberET;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_details);

        init();

    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        orderLineNumberET = (TextInputEditText) findViewById(R.id.orderLineNumber);
        orderNumnberET = (TextInputEditText) findViewById(R.id.orderNumber);
        productCodeET = (TextInputEditText) findViewById(R.id.productCode);
        quantityOrderedET = (TextInputEditText) findViewById(R.id.quantityOrdered);
        priceEachET = (TextInputEditText) findViewById(R.id.priceEach);

        findViewById(R.id.insert).setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        int order_line_number = Integer.valueOf(orderLineNumberET.getText().toString());
        int order_number = Integer.valueOf(orderNumnberET.getText().toString());
        int quantity_ordered = Integer.valueOf(quantityOrderedET.getText().toString());
        int price_each = Integer.valueOf(priceEachET.getText().toString());
        String product_code = productCodeET.getText().toString();


    }
}
