package com.karine.go4lunch.controllers.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements LocationListener {

    public LocationManager locationManager;
    private static final int PERMS_CALL_ID = 200;
    private GoogleMap mMap;
    private String mPosition;



    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }
        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("GPSProvider", "testGPS");
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("PassiveProvider", "testPassive");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 15000, 10, (LocationListener) this);
            Log.e("NetWorkProvider", "testNetwork");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
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
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

        if (mMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();

    }
}