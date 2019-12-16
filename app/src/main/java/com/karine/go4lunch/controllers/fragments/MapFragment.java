package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karine.go4lunch.R;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {



    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


}



//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_toolbar, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//       private void configureToolbar(){
//        //get the toolbar view inside the activity layout
//           ((LoginActivity) Objects.requireNonNull(getActivity().setSupportActionBar(toolbar));


