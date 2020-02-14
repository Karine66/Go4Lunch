package com.karine.go4lunch.views;

import android.content.Context;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.util.Listener;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.controllers.fragments.WorkMatesFragment;
import com.karine.go4lunch.models.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesViewHolder> {



    private List<User> userList;
    private RequestManager glide;
    private User user;
    private String username;
    private User modelUser;



    //Constructor
    public WorkmatesAdapter(FirestoreRecyclerOptions<User> options, RequestManager glide) {
        super(options);
        this.glide = glide;


    }



    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_work_mates_item, parent, false);
        return new WorkmatesViewHolder(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull WorkmatesViewHolder workmatesViewHolder, int i, @NonNull User user) {
        workmatesViewHolder.updateWithDetails(this.glide, this.user);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

    }


//    @Override
//    public int getItemCount() {
//        return this.user.size();
//    }
}
