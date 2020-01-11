package views;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.BuildConfig;
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
    @BindView(R.id.list_photo)
    ImageView mPhoto;

    private List<PlaceDetailsResult> result;
    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;

    public Go4LunchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    //For update details restaurants
    public void updateWithDetails (PlaceDetailsResult result, RequestManager glide) {


        this.mName.setText(result.getName());
        this.mAdress.setText(result.getVicinity());
       //this.mOpenHours.setText(result.getOpeningHours().getPeriods();
        if(result.getPhotos() !=null && !result.getPhotos().isEmpty()){
        glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference()+"&key="+GOOGLE_MAP_API_KEY)
                .apply(RequestOptions.circleCropTransform()).into(mPhoto);
    }else {
        mPhoto.setImageResource(R.drawable.no_picture);
    }
}
//    https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY


}
