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
import com.webianks.anotech.adapters.TestingAdapter;
import com.webianks.anotech.test_classes.AdditionalServer;
import com.webianks.anotech.test_classes.CreditCardFraud;
import com.webianks.anotech.test_classes.ProductPriceZeroAnomaly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class Testing extends Fragment implements TestingAdapter.ItemClickListener {

    private RecyclerView testingRecyclerView;


    public static Testing newInstance() {

        Bundle args = new Bundle();

        Testing fragment = new Testing();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.testing_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        testingRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTesting);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        testingRecyclerView.setLayoutManager(linearLayoutManager);

        List<String> testingList = new ArrayList<>();
        testingList.add("Product Price Zero Anomaly");
        testingList.add("Credit Card Fraud");
        testingList.add("Additional Server Requirement");

        TestingAdapter adapter = new TestingAdapter(getActivity(), testingList);
        testingRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);

    }

    @Override
    public void itemClicked(int position) {

        if(position == 0)
            startActivity(new Intent(getActivity(), ProductPriceZeroAnomaly.class));

        else if(position == 1)
            startActivity(new Intent(getActivity(), CreditCardFraud.class));

        else if(position == 2)
            startActivity(new Intent(getActivity(), AdditionalServer.class));

    }
}
