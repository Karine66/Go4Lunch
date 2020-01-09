package views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karine.go4lunch.R;

import java.util.List;

import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class Go4LunchAdapter extends RecyclerView.Adapter<Go4LunchViewHolder> {


    private List<PlaceDetail> placeDetails;
    private List<PlaceDetailsResult> mDetailResult;
    //Constructor
    public Go4LunchAdapter(List<PlaceDetailsResult> result) {
        this.mDetailResult = result;
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
    viewHolder.updateWithDetails(this.mDetailResult.get(position));
    }
    //return the total count of items in the list
    @Override
    public int getItemCount() {
        return this.mDetailResult.size();
    }
}
