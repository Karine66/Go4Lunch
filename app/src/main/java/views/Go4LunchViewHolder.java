package views;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;


public class Go4LunchViewHolder extends RecyclerView.ViewHolder {


    private float longitude;
    private float latitude;
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
    private String mPosition;
    public boolean openNow;
    public List<ResultSearch> resultSearchList;
    private LatLng mLatitude;
    private LatLng mLongitude;
    private Location location;
    private List<PlaceDetail> placeDetails;
    private LatLng userLatLng;
    private Context context;
    private LocationManager locationManager;


    public Go4LunchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //For update details restaurants
    public void updateWithDetails(PlaceDetailsResult result, RequestManager glide) {


        this.mName.setText(result.getName());
        this.mAdress.setText(result.getVicinity());

        // this.mOpenHours.setText(result.getOpeningHours().getOpenNow().toString());

        //for add photos with Glide
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.circleCropTransform()).into(mPhoto);
        } else {
            mPhoto.setImageResource(R.drawable.no_picture);
        }

        float distance;
        float restoResult[] = new float[10];
        double mLatitude = userLatLng.latitude;
        double mLongitude = userLatLng.latitude;
        double restoLat = result.getGeometry().getLocation().getLat();
        double restoLong = result.getGeometry().getLocation().getLng();
        Location.distanceBetween(mLatitude, mLongitude, restoLat, restoLong, restoResult);
        distance = restoResult[0];
        String dist = Math.round(distance) + "m";
        Log.d("TestDistance", dist);
    }
}









