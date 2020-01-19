package com.karine.go4lunch.controllers.fragments;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karine.go4lunch.R;


import java.util.ArrayList;
import java.util.List;

import Utils.Go4LunchStream;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements LocationListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Location location;
    private Disposable mDisposable;
    private String mPosition;
    private List <ResultSearch> resultSearch;
    private Marker marker;
    private String TAG_LIST_FRAGMENT;


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
//        checkPermissions();
        loadMap();


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
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

//        Toast.makeText(getContext(), "Location" + latitude + "/" + longitude, Toast.LENGTH_LONG).show();
        if (mMap != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);
            executeHttpRequestWithRetrofit();
        }
    }

//    /**
//     * HTTP request RX Java for restaurants
//     */
    private void executeHttpRequestWithRetrofit() {

        this.mDisposable = Go4LunchStream.streamFetchRestaurants(mPosition, 5000, "restaurant")
                .subscribeWith(new DisposableObserver<GoogleApi>() {


                    private List<ResultSearch> resultSearchList = new ArrayList<>();

                    @Override
                    public void onNext(GoogleApi mResultSearches) {

                       resultSearchList.addAll(mResultSearches.getResults());
                       Log.d("TestonNextSize", String.valueOf(resultSearchList.size()));
                    }

                    @Override
                    public void onComplete() {

                        for (ResultSearch res : resultSearchList){
                            LatLng latLng = new LatLng(res.getGeometry().getLocation().getLat(),
                                                       res.getGeometry().getLocation().getLng()
                                                        );
                            marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_markerv2))
                                    .title(res.getName())
                                    .snippet(res.getVicinity()));
                                    marker.showInfoWindow();

                        }
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
