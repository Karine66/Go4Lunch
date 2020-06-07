package com.karine.go4lunch.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.PlaceDetailsAPI.Period;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.karine.go4lunch.utils.DatesAndHours.convertStringToHours;
import static com.karine.go4lunch.utils.DatesAndHours.getCurrentTime;


public class Go4LunchViewHolder extends RecyclerView.ViewHolder {

    //Declarations
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
    @BindView(R.id.list_workmates)
    TextView mWormates;


    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private static final long serialVersionUID = 1L;
    private float[] distanceResults = new float[3];
    private String closeHour;
    private int diff;


    public Go4LunchViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        getCurrentTime();
    }

    /**
     * For update details restaurants
     *
     * @param result
     * @param glide
     * @param mPosition
     */
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

        //for numberWorkmates
        numberWorkmates(result.getPlaceId());

        //for retrieve opening hours (open or closed)
        if (result.getOpeningHours() != null) {

            if (result.getOpeningHours().getOpenNow().toString().equals("false")) {
                this.mOpenHours.setText(R.string.closed);
                this.mOpenHours.setTextColor(Color.RED);
            } else if (result.getOpeningHours().getOpenNow().toString().equals("true")) {
                getHoursInfo(result);
            }
        }
        if (result.getOpeningHours() == null) {
            this.mOpenHours.setText(R.string.opening_hours_not_avalaible);
            this.mOpenHours.setTextColor(Color.BLACK);
        }

        //for add photos with Glide
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + result.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.circleCropTransform()).into(mPhoto);
        } else {
            mPhoto.setImageResource(R.drawable.no_picture);
        }
    }

    /**
     * For calculate restaurant distance
     *
     * @param startLocation
     * @param endLocation
     */
    private void restaurantDistance(String startLocation, com.karine.go4lunch.models.PlaceDetailsAPI.Location endLocation) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        double endLatitude = endLocation.getLat();
        double endLongitude = endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distanceResults);
    }

    /**
     * For rating
     *
     * @param result
     */
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

    /**
     * For hours info (open until, closed, closing soon)
     *
     * @param result
     * @return
     */
    @SuppressLint("SetTextI18n")
    private String getHoursInfo(PlaceDetailsResult result) {
        int[] days = {0, 1, 2, 3, 4, 5, 6};
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if (result.getOpeningHours() != null && result.getOpeningHours().getPeriods() != null) {
            for (Period p : result.getOpeningHours().getPeriods()) {
                String closeHour = p.getClose().getTime();
                Log.d("closeHour", String.valueOf(closeHour));
                int hourClose = Integer.parseInt(closeHour);
                Log.d("hourClose", String.valueOf(hourClose));
                diff = getCurrentTime() - hourClose;
                Log.d("diff", String.valueOf(diff));

                if (p.getOpen().getDay() == days[day] && diff < -100) {
                    mOpenHours.setText(itemView.getContext().getString(R.string.open_until) + " " + convertStringToHours(closeHour));
                    this.mOpenHours.setTextColor(itemView.getContext().getResources().getColor(R.color.colorOpen));
                    Log.d("Open Until", "Open Until" + " " + convertStringToHours(closeHour));

                } else if (diff >= -100 && days[day] == p.getClose().getDay()) {
                    mOpenHours.setText(itemView.getContext().getString(R.string.closing_soon) + " " + "(" + convertStringToHours(closeHour) + ")");
                    this.mOpenHours.setTextColor(itemView.getContext().getResources().getColor(R.color.colorOpen));
                    Log.d("Closing Soon", "closing soon" + convertStringToHours(closeHour));

                }
            }
        }
        return closeHour;
    }

    /**
     * For retrieve number workmates who choose restaurant
     *
     * @param placeId
     */
    private void numberWorkmates(String placeId) {

        UserHelper.getUsersCollection()
                .whereEqualTo("placeId", placeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.d("numberWorkmates", documentSnapshot.getId() + " " + documentSnapshot.getData());
                            }
                            int numberWorkmates = Objects.requireNonNull(task.getResult()).size();
                            String workmatesNumber = "(" + numberWorkmates + ")";
                            mWormates.setText(workmatesNumber);


                        } else {
                            Log.e("numberMatesError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}














