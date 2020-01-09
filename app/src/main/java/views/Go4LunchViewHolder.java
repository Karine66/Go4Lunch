package views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.karine.go4lunch.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;


public class Go4LunchViewHolder extends RecyclerView.ViewHolder {

    //Déclarations
    @BindView(R.id.list_name)
    TextView mName;
    @BindView(R.id.list_adress)
    TextView mAdress;
    @BindView(R.id.list_openhours)
    TextView mOpenHours;

    private List<PlaceDetailsResult> result;


    public Go4LunchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    //For update details restaurants
    public void updateWithDetails (PlaceDetailsResult result) {


        this.mName.setText(result.getName());
        this.mAdress.setText(result.getVicinity());
        this.mOpenHours.setText(result.getOpeningHours().getOpenNow().toString());
    }


}
