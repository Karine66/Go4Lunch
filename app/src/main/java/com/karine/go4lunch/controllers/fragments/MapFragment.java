package com.karine.go4lunch.controllers.fragments;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.karine.go4lunch.controllers.activities.RestaurantActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.karine.go4lunch.Utils.Go4LunchStream;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

import com.karine.go4lunch.models.NearbySearchAPI.ResultSearch;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements LocationListener, Serializable {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Location location;
    private Disposable mDisposable;
    private String mPosition;
    private List<ResultSearch> resultSearch;
    private Marker positionMarker;
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

//for SearchView
        setHasOptionsMenu(true);

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        //For title for this fragment
           getActionBar().setTitle("I'm Hungry");
        }
    //For SearchView
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_main, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
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
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, PERMS_CALL_ID);
                    return;
                }
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

        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(mPosition, 3000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {

                        for (PlaceDetail detail : placeDetails) {
                            LatLng latLng = new LatLng(detail.getResult().getGeometry().getLocation().getLat(),
                                    detail.getResult().getGeometry().getLocation().getLng()
                            );
                            positionMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_markerv2))
                                    .title(detail.getResult().getName())
                                    .snippet(detail.getResult().getVicinity()));
                            positionMarker.showInfoWindow();
                            PlaceDetailsResult placeDetailsResult = detail.getResult();
                            positionMarker.setTag(placeDetailsResult);
                            Log.d("detailResultMap", String.valueOf(placeDetailsResult));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestDetail", Log.getStackTraceString(e));

                    }

                });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override

            public void onInfoWindowClick(Marker marker) {

                PlaceDetailsResult positionMarkerList = (PlaceDetailsResult) positionMarker.getTag();
                Intent intent = new Intent(getContext(), RestaurantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("placeDetailsResult", positionMarkerList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * Dispose subscription
     */
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }
}
