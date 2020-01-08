package views;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.karine.go4lunch.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.Result;

public class Go4LunchViewHolder extends RecyclerView.ViewHolder {

    //Déclarations
    @BindView(R.id.list_name)
    TextView mName;
    @BindView(R.id.list_adress)
    TextView mAdress;
    @BindView(R.id.list_openhours)
    TextView mOpenHours;
    private List<PlaceDetail> resultDetail;


    public Go4LunchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    //For update details restaurants
    public void updateWithDetails (PlaceDetail resultDetail) {


        this.mName.setText(resultDetail.getResult().getName());
        Log.d("Testname", resultDetail.getResult().getName());
        this.mAdress.setText(resultDetail.getResult().getVicinity());
        this.mOpenHours.setText(resultDetail.getResult().getOpeningHours().getOpenNow().toString());
    }
}
