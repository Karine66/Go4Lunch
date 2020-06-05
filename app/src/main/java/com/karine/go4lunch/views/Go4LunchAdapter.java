package com.karine.go4lunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;

import java.util.List;

public class Go4LunchAdapter extends RecyclerView.Adapter<Go4LunchViewHolder> {


    //Declarations
    private String mPosition;
    private RequestManager glide;
    private List<PlaceDetail> placeDetails;

    public void setPosition(String position) {
        mPosition = position;
    }

    /**
     * Constructor
     *
     * @param placeDetails
     * @param glide
     * @param mPosition
     */
    public Go4LunchAdapter(List<PlaceDetail> placeDetails, RequestManager glide, String mPosition) {
        this.placeDetails = placeDetails;
        this.glide = glide;
        this.mPosition = mPosition;
    }

    /**
     * Create viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public Go4LunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);

        return new Go4LunchViewHolder(view);
    }

    /**
     * Update viewHolder with placeDetails
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull Go4LunchViewHolder viewHolder, int position) {
        viewHolder.updateWithDetails(this.placeDetails.get(position).getResult(), this.glide, this.mPosition);
    }

    /**
     * return the total count of items in the list
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return this.placeDetails.size();
    }
}
