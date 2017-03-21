package com.webianks.anotech.fragments;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class DataStructure extends Fragment {

    private RecyclerView recyclerView;
    private List<String> structureList = new ArrayList<String>();

    public static DataStructure newInstance() {

        Bundle args = new Bundle();

        DataStructure fragment = new DataStructure();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.data_structure,container,false);
        init(view);
        return view;
    }

    private void init(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        structureList.add("Employees");
        structureList.add("Offices");
        structureList.add("Products");
        structureList.add("Product Lines");
        structureList.add("Orders");
        structureList.add("Order Details");
        structureList.add("Customers");
        structureList.add("Payments");

        StructureAdapter adapter = new StructureAdapter(getActivity(),structureList);
        recyclerView.setAdapter(adapter);

    }
}
