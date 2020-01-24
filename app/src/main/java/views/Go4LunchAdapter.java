package views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.karine.go4lunch.R;

import java.util.List;

import models.PlaceDetailsAPI.Location;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class Go4LunchAdapter extends RecyclerView.Adapter<Go4LunchViewHolder> {

    public void setPosition(String position) {
        mPosition = position;
    }

    private String mPosition;
    private RequestManager glide;
    private List<PlaceDetail> placeDetails;



    //Constructor
    public Go4LunchAdapter(List<PlaceDetail> placeDetails, RequestManager glide, String mPosition) {
        this.placeDetails = placeDetails;
        this.glide = glide;
        this.mPosition = mPosition;
    }


    @NonNull
    @Override
    public Go4LunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);
        return new Go4LunchViewHolder(view);
    }
    //Update view holer with place details
    @Override
    public void onBindViewHolder(@NonNull Go4LunchViewHolder viewHolder, int position) {
    viewHolder.updateWithDetails(this.placeDetails.get(position).getResult(), this.glide, this.mPosition);
    }
    //return the total count of items in the list
    @Override
    public int getItemCount() {
        return this.placeDetails.size();
    }


}
