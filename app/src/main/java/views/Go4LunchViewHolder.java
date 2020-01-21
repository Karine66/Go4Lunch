package views;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    @BindView(R.id.list_rating)
    RatingBar mRatingBar;


    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private List<PlaceDetailsResult> result;
    private String mPosition;
    private Context context;
    private Location location;
    private String resto;



    public Go4LunchViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

    }



    //For update details restaurants
    public void updateWithDetails(PlaceDetailsResult result, RequestManager glide,String mPosition) {

        mPosition = "";
        //restaurant name
        this.mName.setText(result.getName());
        //restaurant adres
        this.mAdress.setText(result.getVicinity());
        //restaurant rating
        restaurantRating(result);

        // this.mOpenHours.setText(result.getOpeningHours().getOpenNow().toString());

        //for add photos with Glide
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.circleCropTransform()).into(mPhoto);
        } else {
            mPhoto.setImageResource(R.drawable.no_picture);
        }
//        //for restaurant distance
//        restaurantDistance(mPosition, result.getGeometry().getLocation());
//        String distance = Integer.toString(Math.round(distanceResults[0]));
//        Log.d("TestDistance", distance);
//
//
//    }
//
//    //For calculate restaurant Distance
//    private void restaurantDistance(String startLocation, models.PlaceDetailsAPI.Location endLocation) {
//        String[] separatedStart = startLocation.split(",");
//        double startLatitude = Double.parseDouble(separatedStart[0]);
//        double startLongitude = Double.parseDouble(separatedStart[1]);
//        double endLatitude = endLocation.getLat();
//        double endLongitude = endLocation.getLng();
//        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distanceResults);
   }
    //For restaurant Rating
    private void restaurantRating(PlaceDetailsResult result) {
        if (result.getRating() != null) {
            double restaurantRating = result.getRating();
            double rating = (restaurantRating / 5)*3;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);

        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }
    }

//
//        float distance;
//        float restoResult[] = new float[10];
//        double mLatitude = location.getLatitude();
//        double mLongitude = location.getLongitude();
//        double restoLat = result.getGeometry().getLocation().getLat();
//        double restoLong = result.getGeometry().getLocation().getLng();
//        Location.distanceBetween(mLatitude, mLongitude, restoLat, restoLong, restoResult);
//        distance = restoResult[0];
//        String dist = Math.round(distance) + "m";
//        Log.d("TestDistance", dist);

//        Location mPosition = new Location("GPS_PROVIDER");
//        double longitude = 0;
//        double latitude = 0;
//
//        mPosition.setLatitude(latitude);
//        mPosition.setLongitude(longitude);
//
//        Log.d("TestmPosition", String.valueOf(mPosition));
//        Location resto = new Location("GPS_PROVIDER");
//
//        resto.setLatitude(result.getGeometry().getLocation().getLat());
//        resto.setLongitude(result.getGeometry().getLocation().getLng());
//
//        float distance = mPosition.distanceTo(resto);
//
//        Log.d("TestDistance", String.valueOf(distance));
   }













