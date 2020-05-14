package com.karine.go4lunch.controllers.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karine.go4lunch.R;
import com.karine.go4lunch.utils.Go4LunchStream;
import com.karine.go4lunch.utils.ItemClickSupport;
import com.karine.go4lunch.controllers.activities.RestaurantActivity;
import com.karine.go4lunch.models.AutocompleteAPI.AutocompleteResult;
import com.karine.go4lunch.models.AutocompleteAPI.Prediction;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.views.Go4LunchAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment implements Serializable {

    public Disposable mDisposable;
    public List<PlaceDetail> placeDetails;
    @BindView(R.id.fragment_list_RV)
    RecyclerView mRecyclerView;
    private String mPosition;
    private Go4LunchAdapter adapter;
    private Prediction predictions;
    private AutocompleteResult autocompleteResult;
    private String input;


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
                if (newText.isEmpty()) {
                    executeHttpRequestWithRetrofit();
                }
                executeHttpRequestWithRetrofitAutocomplete(newText);
                return true;
            }

        });
    }

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


    public void onLocationChanged(Location location) {
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
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    //Update UI Restaurants
    private void updateUI(List<PlaceDetail> placeDetails) {

        this.placeDetails.clear();
        this.placeDetails.addAll(placeDetails);

        Log.d("TestUI", placeDetails.toString());
        adapter.notifyDataSetChanged();
    }

    /**
     * HTTP request RX Java for autocomplete
     */
    private void executeHttpRequestWithRetrofitAutocomplete(String input) {

        this.mDisposable = Go4LunchStream.streamFetchAutocompleteInfos(input, 2000, mPosition, "establishment")
                .subscribeWith(new DisposableSingleObserver<List<PlaceDetail>>() {

                    @Override
                    public void onSuccess(List<PlaceDetail> placeDetails) {

                        updateUI(placeDetails);

                        Log.d("TestPlaceDetail", String.valueOf(placeDetails.size()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TestAutocomplete", Log.getStackTraceString(e));

                    }
                });
    }
}
