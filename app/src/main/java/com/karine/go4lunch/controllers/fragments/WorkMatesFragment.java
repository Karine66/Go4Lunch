package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import com.karine.go4lunch.Utils.FirebaseUtils;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.views.WorkmatesAdapter;


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
        getCurrentUserFromFirestore();
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
    //Get Current User From firestore
    private void getCurrentUserFromFirestore() {
        Objects.requireNonNull(UserHelper.getAllUsers().get()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task task) {
                task.getResult();
                user.addAll(userList);

            }
//        }) {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                modelUser = documentSnapshot.toObject(User.class);
//            }
        });
    }

    //Configure RecyclerView, Adapter, LayoutManager & glue it
    private void configureRecyclerView() {
        //reset List
       // this.userList = new ArrayList<>();
        this.userList = user;
        //create adapter passing the list of restaurants
        this.workmatesAdapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()), this.glide);
        //Attach the adapter to the recyclerview to items
        this.mRecyclerViewMates.setAdapter(workmatesAdapter);
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

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }

//    //Update UI
//    private void updateUIWhenCreating() {
//
//        UserHelper.getUser(Objects.requireNonNull(Objects.requireNonNull(UserHelper.getAllUsers(currentUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                User currentUser = documentSnapshot.toObject(User.class);
//                assert currentUser != null;
//                String userName = TextUtils.isEmpty(currentUser.getUsername()) ?
//                        ("No username found") : currentUser.getUsername();
//            }
//        }));
//    }
}
