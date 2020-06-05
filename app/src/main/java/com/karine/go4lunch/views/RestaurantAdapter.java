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
import com.karine.go4lunch.models.User;

import butterknife.BindView;

public class RestaurantAdapter extends FirestoreRecyclerAdapter<User, RestaurantViewHolder> {

    @BindView(R.id.resto_mates_name)
    TextView mRestoMatesName;

    private RequestManager glide;

    /**
     * Create constructor
     * @param options
     * @param glide
     */
    public RestaurantAdapter(FirestoreRecyclerOptions<User> options, RequestManager glide) {
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
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_restaurant_item, parent, false);
        return new RestaurantViewHolder(view);
    }


    /**
     * Update View Holder
     * @param restaurantViewHolder
     * @param position
     * @param model
     */
    @Override
    protected void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position, @NonNull User model) {
        restaurantViewHolder.updateWithDetails(model, this.glide);
    }


}
