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

public class Testing extends Fragment {

    public static Testing newInstance() {
        
        Bundle args = new Bundle();
        
        Testing fragment = new Testing();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.testing_fragment,container,false);
        //init(view);
        return view;
    }
}
