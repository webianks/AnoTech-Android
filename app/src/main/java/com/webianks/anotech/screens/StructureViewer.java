package com.webianks.anotech.screens;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.webianks.anotech.R;
import com.webianks.anotech.adapters.TableAttribute;
import com.webianks.anotech.adapters.TableViewerAdapter;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.database.Projections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 22-03-2017.
 */

public class StructureViewer extends AppCompatActivity {

    private RecyclerView tableRecyclerView;

    private String tableNames[] = {
            Contract.TABLE_EMPLOYEES,
            Contract.TABLE_OFFICES,
            Contract.TABLE_PRODUCTS,
            Contract.TABLE_PRODUCT_LINES,
            Contract.TABLE_ORDERS,
            Contract.TABLE_ORDER_DETAILS,
            Contract.TABLE_CUSTOMERS,
            Contract.TABLE_PAYMENTS};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.structure_viewer);

        init();

    }


    private void getColumns(int tableNumber) {

        AnotechDBHelper anotechDBHelper = new AnotechDBHelper(this);
        SQLiteDatabase sqLiteDatabase = anotechDBHelper.getReadableDatabase();


        String tableName = tableNames[tableNumber];

        Cursor dbCursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

      /*  int count = 0;
        if (cursor != null)
            count = cursor.getCount();*/

        setColumnNames(columnNames);

        //Toast.makeText(this, "Number of columns " + cursor.getCount(), Toast.LENGTH_LONG).show();

    }

    private void setColumnNames(String[] columnNames) {


        TableViewerAdapter adapter = new TableViewerAdapter(columnNames, this);
        tableRecyclerView.setAdapter(adapter);
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tableRecyclerView = (RecyclerView) findViewById(R.id.tableRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        tableRecyclerView.setLayoutManager(llm);

        int tableNumber = getIntent().getIntExtra("table_number", 0);

        getColumns(tableNumber);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}
