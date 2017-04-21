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
import com.webianks.anotech.test_classes.CreditCardFraud;
import com.webianks.anotech.test_classes.ProductPriceZeroAnomaly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class Testing extends Fragment implements TestingAdapter.ItemClickListener {

    private RecyclerView testingRecyclerView;
    private List<String> testingList = new ArrayList<>();

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

        testingList.add("Product Price Zero Anomaly");
        testingList.add("Credit Card Fraud");

        TestingAdapter adapter = new TestingAdapter(getActivity(), testingList);
        testingRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);

    }

    @Override
    public void itemClicked(int position) {

        if(testingList.get(position).equals("Product Price Zero Anomaly"))
            startActivity(new Intent(getActivity(), ProductPriceZeroAnomaly.class));

        else if(testingList.get(position).equals("Credit Card Fraud"))
            startActivity(new Intent(getActivity(), CreditCardFraud.class));
    }
}
