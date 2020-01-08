package views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.karine.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import models.PlaceDetailsAPI.PlaceDetail;


public class Go4LunchViewHolder extends RecyclerView.ViewHolder {

    //Déclarations
    @BindView(R.id.list_name)
    TextView mName;
    @BindView(R.id.list_adress)
    TextView mAdress;
    @BindView(R.id.list_openhours)
    TextView mOpenHours;



    public Go4LunchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    //For update details restaurants
    public void updateWithDetails (PlaceDetail placeDetails) {


        this.mName.setText(placeDetails.getResult().getName());
        Log.d("Testname", placeDetails.getResult().getName());
        this.mAdress.setText(placeDetails.getResult().getVicinity());
        this.mOpenHours.setText(placeDetails.getResult().getOpeningHours().getOpenNow().toString());
    }
}
