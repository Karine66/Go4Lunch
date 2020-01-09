package com.karine.go4lunch.controllers.fragments;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Utils.Go4LunchStream;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;
import views.Go4LunchAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements LocationListener {

    public Disposable mDisposable;
    private String mPosition;
    public List<PlaceDetail> placeDetails;
    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
    private Object provider;
    private Go4LunchAdapter adapter;
    public List<PlaceDetailsResult> resultDetail;
    @BindView(R.id.fragment_list_RV)
    RecyclerView mRecyclerView;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();

       // executeHttpRequestWithRetrofit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
    //Configuration RV
    //Configure RecyclerView, Adapter, LayoutManager & glue it
    private void configureRecyclerView() {
        //reset List
        this.resultDetail = new ArrayList<>();
        //create adapter passing the list of users
        this.adapter = new Go4LunchAdapter(this.resultDetail);
        //Attach the adapter to the recyclerview to items
        this.mRecyclerView.setAdapter(adapter);
        //Set layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                mPosition = mLatitude + "," + mLongitude;
                Log.d("TestListPosition", mPosition);
                executeHttpRequestWithRetrofit();
            }
//        }
    /**
     * HTTP request RX Java for restaurants
     */
    private void executeHttpRequestWithRetrofit() {


        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(mPosition, 5000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(List<PlaceDetail> resultDetail) {

//                        resultDetail.addAll(placeDetails);
                        //update RV after getting results
                       updateUI(resultDetail);


                   Log.d("TestPlaceDetail", String.valueOf(resultDetail.size()));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestDetail", Log.getStackTraceString(e));

                    }
                });
    }
        private void disposeWhenDestroy() {
        if(this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
        }

        //Update UI
    private void updateUI(List<PlaceDetail> resultDetail) {
       // resultDetail.clear();
//       resultDetail.addAll(placeDetails);
        Log.d("TestUI", resultDetail.toString());
        adapter.notifyDataSetChanged();
    }
}
