package com.webianks.anotech.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webianks.anotech.R;

/**
 * Created by R Ankit on 21-03-2017.
 */

public class DataStructure extends Fragment {

    public static DataStructure newInstance() {

        Bundle args = new Bundle();

        DataStructure fragment = new DataStructure();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_structure,container,false);
    }
}
