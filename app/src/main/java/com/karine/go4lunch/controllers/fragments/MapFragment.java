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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karine.go4lunch.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Utils.Go4LunchStream;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private static final int PERMS_CALL_ID = 200;
    private Location location;
    private Disposable mDisposable;
    private String position;
    private List <ResultSearch> resultSearch;
    private Marker marker;


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

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
        loadMap();

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
                    LocationManager.GPS_PROVIDER, 15000, 10, this);
            Log.e("GPSProvider", "test");
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, this);
            Log.e("PassiveProvider", "test");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 15000, 10, this);
            Log.e("NetWorkProvider", "test");
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

    private void loadMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                googleMap.setMyLocationEnabled(true);

            }
        });
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
        Log.d("LocationProject", "Provider Enabled");
    }



    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

//        Toast.makeText(getContext(), "Location" + latitude + "/" + longitude, Toast.LENGTH_LONG).show();
        if (mMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            position = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", position);
            executeHttpRequestWithRetrofit();
        }
    }



    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

//    /**
//     * HTTP request RX Java for restaurants
//     */
    private void executeHttpRequestWithRetrofit() {
//        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(loc, 5000, "restaurant")
//             .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {
//
//                 @Override
//                 public void onSuccess(List<PlaceDetail> placeDetails) {
//                     Log.d("TestDetails", String.valueOf(placeDetails.size()));
//                 }
//
//                 @Override
//                 public void onError(Throwable e) {
//                     Log.e("TestDetail", Log.getStackTraceString(e));
//
//                 }
//             });
        this.mDisposable = Go4LunchStream.streamFetchRestaurants(position, 5000, "restaurant")
                .subscribeWith(new DisposableObserver<GoogleApi>() {


                    private List<ResultSearch> resultSearchList = new ArrayList<>();

                    @Override
                    public void onNext(GoogleApi mResultSearches) {
                        Log.d("TestonNextMap", mResultSearches.toString());
                       resultSearchList.addAll(mResultSearches.getResults());
                       Log.d("TestonNextSize", String.valueOf(resultSearchList.size()));
                    }

                    @Override
                    public void onComplete() {

                        for (ResultSearch res : resultSearchList){
                            LatLng latLng = new LatLng(res.getGeometry().getLocation().getLat(),
                                                       res.getGeometry().getLocation().getLng()
                                                        );
                            marker = mMap.addMarker(new MarkerOptions().position(latLng));

                        }

                        Log.d("TestOnComleteMap", String.valueOf(resultSearchList.size()));


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onErrorRestaurantsMap", Log.getStackTraceString(e));
                    }
                });
    }
////updte UI with restaurants list
//    public void updateUI(List <ResultSearch> results) {
//
//       results.clear();
//        results.addAll(results);
//
//    }

    /**
     * Dispose subscription
     */
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }
}
