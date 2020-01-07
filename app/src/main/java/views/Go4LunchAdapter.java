package views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karine.go4lunch.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.Result;

public class Go4LunchAdapter extends RecyclerView.Adapter<Go4LunchViewHolder> {

    private List<PlaceDetail> placeDetails;
    private Result mResult;
    private List<PlaceDetail> resultDetail;
    //Constructor
    public Go4LunchAdapter(List<PlaceDetail> resultDetail) {
        this.resultDetail = resultDetail;
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
    viewHolder.updateWithDetails(this.resultDetail.get(position));
    }
    //return the total count of items in the list
    @Override
    public int getItemCount() {
        return this.resultDetail.size();
    }
}
