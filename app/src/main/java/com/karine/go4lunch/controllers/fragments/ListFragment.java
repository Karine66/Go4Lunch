package com.karine.go4lunch.controllers.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import java.util.Objects;

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
public class ListFragment extends Fragment implements LocationListener {

    public Disposable mDisposable;
    private String position;
    public List<PlaceDetail> placeDetails;
    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
    private Object provider;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);


       // executeHttpRequestWithRetrofit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
//

    }
    @SuppressWarnings("MissingPermission")
    private void checkPermissions() {

        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 15000, 10, this);
            Log.e("GPSProvider", "testGPS");
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, this);
            Log.e("PassiveProvider", "testPassive");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 15000, 10, this);
            Log.e("NetWorkProvider", "testNetwork");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
            Log.d("LocationProject", "Provider Enabled");
        }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

        public void onLocationChanged(Location location){
            double mLatitude = location.getLatitude();
            double mLongitude = location.getLongitude();
                position = mLatitude + "," + mLongitude;
                Log.d("TestListPosition", position);
                executeHttpRequestWithRetrofit();
            }
//        }
    /**
     * HTTP request RX Java for restaurants
     */
    private void executeHttpRequestWithRetrofit() {


        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(position, 5000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {



                   Log.d("TestPlaceDetail", String.valueOf(placeDetails.get(0)));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestDetail", Log.getStackTraceString(e));

                    }
                });
    }


}
