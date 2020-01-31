package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class RestaurantActivity extends AppCompatActivity implements Serializable {

    @BindView(R.id.resto_photo)
    ImageView mRestoPhoto;
    @BindView(R.id.resto_name)
    TextView mRestoName;
    @BindView(R.id.resto_address)
    TextView mRestoAddress;

    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private PlaceDetailsResult placeDetailsResult;
    private RequestManager glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        PlaceDetailsResult placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");

        assert placeDetailsResult != null;
        Log.d("RestoActivity", placeDetailsResult.getName());
        updateUI(placeDetailsResult, glide);
    }

    private void updateUI(PlaceDetailsResult placeDetailsResult, RequestManager glide) {

        mRestoName.setText(placeDetailsResult.getName());
        mRestoAddress.setText(placeDetailsResult.getVicinity());
        //for add photos with Glide
        if (placeDetailsResult.getPhotos() != null && !placeDetailsResult.getPhotos().isEmpty()) {
            glide.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + placeDetailsResult.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.centerCropTransform()).into(mRestoPhoto);
        } else {
            mRestoPhoto.setImageResource(R.drawable.no_picture);
        }

    }
}
