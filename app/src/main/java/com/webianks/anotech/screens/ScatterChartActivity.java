package com.webianks.anotech.screens;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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


        File root = new File(Environment.getExternalStorageDirectory(), "Anotech");
        File filepath = new File(root, "orders_date_difference.csv");
        ArrayList<Entry> entries = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {

                String[] splittedLine = line.split(",");

                if (splittedLine.length == 2 && Integer.valueOf(splittedLine[1]) > 0)
                    entries.add(new Entry(Integer.valueOf(splittedLine[1]), i++));

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }


        ScatterDataSet dataset = new ScatterDataSet(entries, "# of days");
        dataset.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("0");
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");

        ScatterData data = new ScatterData(labels, dataset);
        mChart.setData(data);

    }

}
