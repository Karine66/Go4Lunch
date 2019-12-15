package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karine.go4lunch.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesFragment extends Fragment {


    public WorkMatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_mates, container, false);
    }

}
