package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import com.karine.go4lunch.models.User;
import com.karine.go4lunch.views.WorkmatesAdapter;
import com.karine.go4lunch.views.WorkmatesViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkMatesFragment extends BaseFragment {


    @BindView(R.id.fragment_Workmates_RV)
    RecyclerView mRecyclerViewMates;


    private List<User> userList;
    private Disposable mDisposable;
    private WorkmatesAdapter workmatesAdapter;
    private RequestManager glide;
    private List<User> user;
    private String username;
    private User modelUser;
    private String currentUserName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = db.collection("users");
    private FirestoreRecyclerOptions<User> userlist;
    private Query query;

    public WorkMatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        ButterKnife.bind(this, view);

//        configureRecyclerView();
 //       updateUIWhenCreating();
        setUpRecyclerView();
        return view;

    }

    private void setUpRecyclerView() {
        Query query = collectionUsers.orderBy("username", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.workmatesAdapter = new WorkmatesAdapter(options);
//        mRecyclerViewMates.setHasFixedSize(true);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }


//    @Override
//    public void onSuccess(DocumentSnapshot documentSnapshot) {
//        modelUser = documentSnapshot.toObject(User.class);
//    }


////        Configure RecyclerView, Adapter, LayoutManager & glue it
//    private void configureRecyclerView() {
//        //reset List
////
//        //  this.userList = user;
//        this.currentUserName = username;
//        //create adapter passing the list of restaurants

//        this.workmatesAdapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()));
//        //Attach the adapter to the recyclerview to items
//       this.mRecyclerViewMates.setAdapter(workmatesAdapter);
////        //Set layout manager to position the items
//       this.mRecyclerViewMates.setLayoutManager(new LinearLayoutManager(getActivity()));
//    }


    @Override
    public void onResume() {
        super.onResume();


    }

    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

//    // Create options for RecyclerView from a Query
//    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query) {
//        return new FirestoreRecyclerOptions.Builder<User>()
//                .setQuery(query, User.class)
//                .setLifecycleOwner(this)
//                .build();
//    }

    //Update UI
    private void updateUIWhenCreating() {

            collectionUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        Log.d("RV", "Error getting document :", task.getException());
                    }
                }

            });
        }
    }

