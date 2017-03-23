package com.webianks.anotech.test_classes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.webianks.anotech.R;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;

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

        String order_line_number = orderLineNumberET.getText().toString();
        String order_number = orderNumnberET.getText().toString();
        String quantity_ordered = quantityOrderedET.getText().toString();
        String price_each = priceEachET.getText().toString();
        String product_code = productCodeET.getText().toString();

        if (order_line_number.trim().length() > 0 &&
                order_number.trim().length() > 0 &&
                quantity_ordered.trim().length() >0 &&
                price_each.trim().length()>0 &&
                product_code.trim().length()>0){


            AnotechDBHelper dbHelper = new AnotechDBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.OrderDetailsEntry.ORDER_LINE_NUMBER,Integer.valueOf(order_line_number));
            contentValues.put(Contract.OrderDetailsEntry.ORDER_NUMBER,Integer.valueOf(order_number));
            contentValues.put(Contract.OrderDetailsEntry.QUANTITY_ORDERED,Integer.valueOf(quantity_ordered));
            contentValues.put(Contract.OrderDetailsEntry.PRICE_EACH,Integer.valueOf(price_each));
            contentValues.put(Contract.OrderDetailsEntry.PRODUCT_CODE,product_code);

            long code = database.insert(Contract.TABLE_ORDER_DETAILS,null,contentValues);

            if (code > 0)
               Toast.makeText(this,getString(R.string.done),Toast.LENGTH_LONG).show();

        }

    }
}
