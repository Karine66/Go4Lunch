package com.karine.go4lunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.User;

import butterknife.BindView;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesViewHolder> {

    @BindView(R.id.workmates_name)
    TextView mWorkmatesName;

    private RequestManager glide;
    private User model;



    /**
     * Create constructor
     * @param options
     * @param glide
     */
    public WorkmatesAdapter(FirestoreRecyclerOptions<User> options, RequestManager glide) {
        super(options);
        this.glide = glide;
    }

    /**
     * Create ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_work_mates_item, parent, false);
        return new WorkmatesViewHolder(view);
    }


    /**
     * Update viewHolder
     * @param workmatesViewHolder
     * @param position
     * @param model
     */
    @Override
    protected void onBindViewHolder(@NonNull WorkmatesViewHolder workmatesViewHolder, int position, @NonNull User model) {
        workmatesViewHolder.updateWithDetails(model, this.glide);
    }


}
