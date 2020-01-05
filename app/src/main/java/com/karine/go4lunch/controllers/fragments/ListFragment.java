package com.karine.go4lunch.controllers.fragments;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Utils.Go4LunchStream;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.Result;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    public Disposable mDisposable;
    private String position;
    public List<PlaceDetail> placeDetails;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        executeHttpRequestWithRetrofit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);


    }

    /**
     * HTTP request RX Java for restaurants
     */
    private void executeHttpRequestWithRetrofit() {


        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(position, 5000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {
                    
                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {

                    Log.d("TestPosition", position);
                   Log.d("TestPlaceDetail", String.valueOf(placeDetails.size()));



                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestDetail", Log.getStackTraceString(e));

                    }
                });
    }


}
