package com.webianks.anotech.screens;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.webianks.anotech.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by R Ankit on 26-03-2017.
 */

public class ResultsActivity extends AppCompatActivity {

    private TextView reason;
    private TextView outliers;
    private ScatterChart mChart;
    private String type;
    private String reasonValue;
    private String outliersValue;
    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_result);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Anomaly Results");

        mChart = (ScatterChart) findViewById(R.id.chart1);
        reason = (TextView) findViewById(R.id.reason);
        outliers = (TextView) findViewById(R.id.outliers);

        type = getIntent().getStringExtra("type");
        reasonValue = getIntent().getStringExtra("reason");
        outliersValue = getIntent().getStringExtra("outlier");
        fileName = getIntent().getStringExtra("file");

        reason.setText(reasonValue);
        outliers.setText(outliersValue);

        if (type.equals("wrong_transactions") || type.equals("credit_card") || type.equals("server_crash")) {
            mChart.setVisibility(View.GONE);
        } else
            setChart();

    }

    private void setChart() {

        File root = new File(Environment.getExternalStorageDirectory(), "Anotech");
        File filepath = new File(root, fileName);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {

                String[] splittedLine = line.split(",");

                if (splittedLine.length == 2 && Integer.valueOf(splittedLine[1]) > 0) {
                    entries.add(new Entry(Float.valueOf(splittedLine[1]), i++));
                    labels.add(splittedLine[0]);
                }

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        String label = " ";
        if (type.equals("orders"))
            label = "# of days";
        else if (type.equals("product_price"))
            label = "# of orders";


        ScatterDataSet dataset = new ScatterDataSet(entries, label);
        dataset.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        ScatterData data = new ScatterData(labels, dataset);
        mChart.setData(data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
