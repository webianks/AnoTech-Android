package com.webianks.anotech.screens;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.webianks.anotech.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by R Ankit on 26-03-2017.
 */

public class ScatterChartActivity extends AppCompatActivity {


    private ScatterChart mChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scatter_chart);

        mChart = (ScatterChart) findViewById(R.id.chart1);

        String type = getIntent().getStringExtra("type");

        String fileName = " ";
        if (type.equals("orders"))
            fileName = "orders_date_difference.csv";
        else if (type.equals("product_price"))
            fileName = "orders_count_on_day.csv";

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

}
