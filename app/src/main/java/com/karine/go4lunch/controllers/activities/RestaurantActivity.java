package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.karine.go4lunch.R;
import com.karine.go4lunch.controllers.fragments.ListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class RestaurantActivity extends AppCompatActivity implements Serializable {

    @BindView(R.id.resto_photo)
    ImageView mRestoPhoto;
    @BindView(R.id.resto_name)
    TextView mRestoName;

    private PlaceDetailsResult placeDetailsResult;

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
        Log.d("Resto", "resto name"+ placeDetailsResult.getName());

    }

//    private void updateUI() {
//
//        mRestoName.setText();
//
//    }
}
