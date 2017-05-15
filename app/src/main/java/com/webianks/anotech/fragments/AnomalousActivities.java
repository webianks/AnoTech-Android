package com.webianks.anotech.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webianks.anotech.R;
import com.webianks.anotech.adapters.AnomaliesAdapter;
import com.webianks.anotech.adapters.Anomaly;
import com.webianks.anotech.database.AnotechDBHelper;
import com.webianks.anotech.database.Contract;
import com.webianks.anotech.screens.ResultsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class AnomalousActivities extends Fragment implements AnomaliesAdapter.ItemClickListener {

    private RecyclerView recyclerview;

    public static AnomalousActivities newInstance() {

        Bundle args = new Bundle();

        AnomalousActivities fragment = new AnomalousActivities();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.anomalous_activities, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);

        getAnomalies();

        return view;
    }

    private void getAnomalies() {

        AnotechDBHelper dbHelper = new AnotechDBHelper(getActivity());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(Contract.TABLE_ANOMALY, null, null, null, null, null, null);

        List<Anomaly> anomalies = new ArrayList<>();

        while (cursor.moveToNext()) {

            int type_index = cursor.getColumnIndex(Contract.AnomalyEntry.TYPE);
            int file_index = cursor.getColumnIndex(Contract.AnomalyEntry.FILE);
            int reason_index = cursor.getColumnIndex(Contract.AnomalyEntry.REASON);
            int outlier_index = cursor.getColumnIndex(Contract.AnomalyEntry.OUTLIER);


            String type = cursor.getString(type_index);
            String file = cursor.getString(file_index);
            String reason = cursor.getString(reason_index);
            String outlier = cursor.getString(outlier_index);

            Anomaly anomaly = new Anomaly();
            anomaly.setType(type);
            anomaly.setFile(file);
            anomaly.setReason(reason);
            anomaly.setOutlier(outlier);

            anomalies.add(anomaly);
        }

        setAdapter(anomalies);
    }

    private void setAdapter(List<Anomaly> anomalies) {

        AnomaliesAdapter anomaliesAdapter = new AnomaliesAdapter(getActivity(), anomalies);
        anomaliesAdapter.setItemClickListener(this);
        recyclerview.setAdapter(anomaliesAdapter);
    }

    @Override
    public void itemClicked(Anomaly anomaly) {

        Intent intent = new Intent(getActivity(), ResultsActivity.class);
        intent.putExtra("type", anomaly.getType());
        intent.putExtra("file", anomaly.getFile());
        intent.putExtra("reason", anomaly.getReason());
        intent.putExtra("outlier", anomaly.getOutlier());
        startActivity(intent);

    }
}
