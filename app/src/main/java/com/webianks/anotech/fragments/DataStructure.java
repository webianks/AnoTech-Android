package com.webianks.anotech.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webianks.anotech.R;
import com.webianks.anotech.adapters.StructureAdapter;
import com.webianks.anotech.screens.StructureViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class DataStructure extends Fragment implements StructureAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    public static DataStructure newInstance() {

        Bundle args = new Bundle();

        DataStructure fragment = new DataStructure();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.data_structure, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        List<String> structureList = new ArrayList<String>();
        structureList.add("Employees");
        structureList.add("Offices");
        structureList.add("Products");
        structureList.add("Product Lines");
        structureList.add("Orders");
        structureList.add("Order Details");
        structureList.add("Customers");
        structureList.add("Payments");

        StructureAdapter adapter = new StructureAdapter(getActivity(), structureList);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(getActivity(), StructureViewer.class);
        intent.putExtra("table_number", position);
        startActivity(intent);
    }
}
