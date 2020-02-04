package com.karine.go4lunch.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class RestaurantActivity extends AppCompatActivity implements Serializable {

    private static final int REQUEST_CALL = 100;
    @BindView(R.id.resto_photo)
    ImageView mRestoPhoto;
    @BindView(R.id.resto_name)
    TextView mRestoName;
    @BindView(R.id.resto_address)
    TextView mRestoAddress;
    @BindView(R.id.call_btn)
    Button mCallBtn;
    @BindView(R.id.web_btn)
    Button mWebBtn;

    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private PlaceDetailsResult placeDetailsResult;
    private RequestManager glide;
    private String formattedPhoneNumber;
    private String url;
    private RequestManager mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        this.retrieveData();

    }

    //For retrieve data to ListFragment
    private void retrieveData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        assertNotNull(bundle);
        PlaceDetailsResult placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
//        Log.d("RestoActivity", placeDetailsResult.getName());
        assert placeDetailsResult != null;
        updateUI(placeDetailsResult, mGlide);

    }
    //For AssertNotNull
    public static <T> T assertNotNull(T bundle) {
        if(bundle == null)
            throw new AssertionError("Object cannot be null");
            return bundle;
        }



    private void updateUI(PlaceDetailsResult placeDetailsResult, RequestManager glide) {
        mGlide = glide;

        //for add photos with Glide
        Glide.with(this)
       .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + placeDetailsResult.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestoPhoto);

        //For Restaurant Name
        mRestoName.setText(placeDetailsResult.getName());
        //For Restaurant address
        mRestoAddress.setText(placeDetailsResult.getVicinity());
        //For  restaurant telephone number
        String formattedPhoneNumber = placeDetailsResult.getFormattedPhoneNumber();
//        Log.d("phoneNumber", formattedPhoneNumber);
        callBtn(formattedPhoneNumber);

        String url = placeDetailsResult.getWebsite();
//        Log.d("website", url);
        webBtn(url);

    }

    //For click call button
    public void callBtn(String formattedPhoneNumber) {
        mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(formattedPhoneNumber);
            }
        });
    }

    //For call and permission
    private void makePhoneCall(String formattedPhoneNumber) {

        if (ContextCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RestaurantActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else if (formattedPhoneNumber != null && !formattedPhoneNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + formattedPhoneNumber));
            Log.d("PhoneNumber", formattedPhoneNumber);
            startActivity(intent);
        } else {
            Toast.makeText(RestaurantActivity.this, "No Phone Available", Toast.LENGTH_SHORT).show();
        }
    }

    //For permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(formattedPhoneNumber);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //For click website button
    public void webBtn(String url) {
        mWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeWebView(url);
            }
        });
    }

    public void makeWebView(String url) {
        if(url!=null && !url.isEmpty()) {
            Intent intent = new Intent(RestaurantActivity.this, WebViewActivity.class);
            intent.putExtra("website", url);
             Log.d("Website", url);
            startActivity(intent);
        }else{
            Toast.makeText(this, "No Website",Toast.LENGTH_SHORT).show();
        }

    }

}
