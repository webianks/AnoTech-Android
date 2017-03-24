package com.webianks.anotech.test_classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.webianks.anotech.R;

/**
 * Created by R Ankit on 24-03-2017.
 */

public class Orders extends AppCompatActivity {


    private TextInputEditText orderNumnberET;
    private TextInputEditText orderDateET;
    private TextInputEditText requiredDateET;
    private TextInputEditText shppedDateET;
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
    }

}
