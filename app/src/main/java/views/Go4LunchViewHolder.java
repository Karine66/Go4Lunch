package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.PlaceDetailsAPI.Period;
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
    @BindView(R.id.list_distance)
    TextView mDistance;


    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private static final long serialVersionUID = 1L;
    private List<PlaceDetailsResult> result;
    private String mPosition;
    private Context context;
    private Location location;
    private String resto;
    private float[] distanceResults = new float[3];
    private Period periods;
    private String localTime;
    private boolean permanentalyClosed;


    public Go4LunchViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        getTodayDate();
        getCurrentTime();
    //    currentDateHour();
//        getHoursInfo((PlaceDetailsResult) result);

    }


    //For update details restaurants
    @SuppressLint("SetTextI18n")
    public void updateWithDetails(PlaceDetailsResult result, RequestManager glide, String mPosition) {


        //restaurant name
        this.mName.setText(result.getName());
        //restaurant adress
        this.mAdress.setText(result.getVicinity());
        //restaurant rating
        restaurantRating(result);
        //restaurant distance
        restaurantDistance(mPosition, result.getGeometry().getLocation());
        String distance = Integer.toString(Math.round(distanceResults[0])) + "m";
        this.mDistance.setText(distance);
        Log.d("TestDistance", distance);

        //for retrieve opening hours (open or closed)
       // String openingHour = Arrays.toString(result.getOpeningHours().getWeekdayText());
        if (result.getOpeningHours() != null) {
            if (result.getOpeningHours().getOpenNow().toString().equals("false")) {
                this.mOpenHours.setText("Closed");
                this.mOpenHours.setTextColor(Color.RED);
            } else if (result.getOpeningHours().getOpenNow().toString().equals("true")) {
                this.mOpenHours.setText("Open");
                // this.mOpenHours.setText("Open" + " "+ openingHour);
                this.mOpenHours.setTextColor(itemView.getContext().getResources().getColor(R.color.colorOpen));
            }
        }
               if (result.getOpeningHours() == null){
                this.mOpenHours.setText("Opening Hours not available");
                this.mOpenHours.setTextColor(Color.BLACK);
            }
//               if (result.getOpeningHours().getPermanentlyClosed().toString().equals("true") ){
//                this.mOpenHours.setText("Permanently Closed");
//                this.mOpenHours.setTextColor(Color.RED);
//            }

     //   getHoursInfo(result);


//        Log.d("TestHours", result.getOpeningHours().toString());
        //for add photos with Glide
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.circleCropTransform()).into(mPhoto);
        } else {
            mPhoto.setImageResource(R.drawable.no_picture);
        }
    }



    //For calculate restaurant Distance
    private void restaurantDistance(String startLocation, models.PlaceDetailsAPI.Location endLocation) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        double endLatitude = endLocation.getLat();
        double endLongitude = endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distanceResults);
   }
       //For restaurant Rating
    private void restaurantRating(PlaceDetailsResult result) {
        if (result.getRating() != null) {
            double restaurantRating = result.getRating();
            double rating = (restaurantRating / 5) * 3;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);

        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }
    }
        private void getHoursInfo(PlaceDetailsResult result) {
        int[] days = {0, 1, 2, 3, 4, 5, 6};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String currentHourString = Integer.toString(hour)+Integer.toString(minute);
        int currentHour = Integer.parseInt(currentHourString);


            for(int i=0; i<result.getOpeningHours().getPeriods().size();i++) {
      //      for(Period p : result.getOpeningHours().getPeriods().size()) {
               if(result.getOpeningHours().getPeriods().get(i).getOpen().getDay() == days [day]){
                   String closeHour = result.getOpeningHours().getPeriods().get(i).getClose().getTime();
                   Log.d("closeHour", closeHour);
               }
                    }

                }

    private void getTodayDate(){
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        @SuppressLint("SimpleDateFormat")
        DateFormat date = new SimpleDateFormat("dd-MM-yyy z");
        String dayDate = date.format(currentDate);
        Log.d("TestDate", dayDate);
    }

    public void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat")
        DateFormat date = new SimpleDateFormat("HH:mm z");
        String localTime = date.format(currentLocalTime);
        Log.d("TestHour", localTime);


    }
//    public void currentDateHour() {
//    Calendar cal = Calendar.getInstance();
//    Date currentLocalTime = cal.getTime();
//
//    @SuppressLint("SimpleDateFormat")
//    DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm z");
//
//    String localTime = date.format(currentLocalTime);
//    Log.d("TestDateHour", localTime);
//   }
}













