package com.karine.go4lunch.controllers.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.ItemClickSupport;
import com.karine.go4lunch.controllers.activities.MainPageActivity;
import com.karine.go4lunch.controllers.activities.RestaurantActivity;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.views.WorkmatesAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.Serializable;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesFragment extends BaseFragment {
    /**
     * Déclarations
     */
    @BindView(R.id.fragment_Workmates_RV)
    RecyclerView mRecyclerViewMates;

    private Disposable mDisposable;
    private WorkmatesAdapter workmatesAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = db.collection("users");
    private PlaceDetailsResult placeDetailsResult;
    public List<PlaceDetail> placeDetails;



    /**
     * Constructor
     */
    public WorkMatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        ButterKnife.bind(this, view);

        setUpRecyclerView();
//        retrieveData();
//        configureOnClickRecyclerView();
        return view;
    }
//
//    /**
//     * For change title action for only this fragment
//     * @param savedInstanceState
//     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActionBar().setTitle("Available Workmates");

    }


    /**
     * RecyclerView configuration
     * Configure RecyclerView, Adapter, LayoutManager & glue it
     */
    private void setUpRecyclerView() {
        Query query = collectionUsers.orderBy("placeId", Query.Direction.DESCENDING);
//        Query query = collectionUsers.orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.workmatesAdapter = new WorkmatesAdapter(options, Glide.with(this));
        mRecyclerViewMates.setHasFixedSize(true);
        mRecyclerViewMates.setAdapter(workmatesAdapter);
        mRecyclerViewMates.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onStart() {
        super.onStart();
        workmatesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        workmatesAdapter.stopListening();
    }



    /**
     * dispose subscription
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * dispose subscription
     */
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }
}

