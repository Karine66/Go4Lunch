package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;


import com.google.firebase.firestore.auth.User;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

import Utils.FirebaseUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import views.WorkmatesAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesFragment extends BaseFragment {


    @BindView(R.id.fragment_Workmates_RV)
    RecyclerView mRecyclerViewMates;


    private List<User> userList;
    private Disposable mDisposable;
    private WorkmatesAdapter adapter;
    private RequestManager glide;

    public WorkMatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        ButterKnife.bind(this, view);
        configureRecyclerView();
        updateUI(userList);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //Configure RecyclerView, Adapter, LayoutManager & glue it
    private void configureRecyclerView() {
        //reset List
        this.userList = new ArrayList<>();
        //create adapter passing the list of restaurants
        this.adapter = new WorkmatesAdapter(this.userList, Glide.with(this));
        //Attach the adapter to the recyclerview to items
        this.mRecyclerViewMates.setAdapter(adapter);
        //Set layout manager to position the items
        this.mRecyclerViewMates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    //Update UI
    private void updateUI(List<User> userList) {
       // this.userList.clear();
        if(FirebaseUtils.isCurrentUserLogged()) {
            this.userList.addAll(userList);
        }
        Log.d("TestUIWorkmate", userList.toString());
        adapter.notifyDataSetChanged();
    }
}