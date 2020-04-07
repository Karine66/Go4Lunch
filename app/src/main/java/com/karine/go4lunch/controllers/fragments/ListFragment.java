package com.karine.go4lunch.controllers.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karine.go4lunch.R;
import com.karine.go4lunch.controllers.activities.MainPageActivity;
import com.karine.go4lunch.controllers.activities.RestaurantActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.karine.go4lunch.Utils.Go4LunchStream;
import com.karine.go4lunch.Utils.ItemClickSupport;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.views.Go4LunchAdapter;



/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment implements Serializable {

    public Disposable mDisposable;
    private String mPosition;
    public List<PlaceDetail> placeDetails;
    private GoogleMap mMap;
    private Location location;
    public LocationManager locationManager;
    private Object provider;
    private Go4LunchAdapter adapter;
   private List<PlaceDetailsResult> result;


    @BindView(R.id.fragment_list_RV)
    RecyclerView mRecyclerView;
//    @BindView(R.id.searchView)
//    SearchView searchView;

    private String  placeId;
    private Context mContext;


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
        this.configureOnClickRecyclerView();
        //for SearchView
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
//       For title action bar for this fragment
         getActionBar().setTitle("I'm Hungry");
        }

        //For SearchView
    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_main,menu);
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
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }


//    //For SearchView Listener
//    public void searchViewQueryListener () {
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
    //Configure RecyclerView, Adapter, LayoutManager & glue it
    private void configureRecyclerView() {
        //reset List
        this.placeDetails = new ArrayList<>();
        //create adapter passing the list of restaurants
        this.adapter = new Go4LunchAdapter(this.placeDetails, Glide.with(this), this.mPosition);
        //Attach the adapter to the recyclerview to items
        this.mRecyclerView.setAdapter(adapter);
        //Set layout manager to position the items
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //Configure item click on RecyclerView
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener((new ItemClickSupport.OnItemClickListener() {

                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        PlaceDetailsResult placeDetailsResult = placeDetails.get(position).getResult();
                        Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("placeDetailsResult", placeDetailsResult);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }));
    }


    public void onLocationChanged(Location location){
            double mLatitude = location.getLatitude();
            double mLongitude = location.getLongitude();
                mPosition = mLatitude + "," + mLongitude;
                Log.d("TestListPosition", mPosition);
                adapter.setPosition(mPosition);
                executeHttpRequestWithRetrofit();
            }

    /**
     * HTTP request RX Java for restaurants
     */
    private void executeHttpRequestWithRetrofit() {

        this.mDisposable = Go4LunchStream.streamFetchRestaurantDetails(mPosition, 2000, "restaurant")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {

                       updateUI(placeDetails);

                   Log.d("TestPlaceDetail", String.valueOf(placeDetails.size()));

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
    private void updateUI(List<PlaceDetail> placeDetails) {
     this.placeDetails.clear();
     this.placeDetails.addAll(placeDetails);

        Log.d("TestUI", placeDetails.toString());
        adapter.notifyDataSetChanged();
    }
}
