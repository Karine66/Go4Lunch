package com.karine.go4lunch.controllers.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.BuildConfig;
import com.karine.go4lunch.R;

import java.io.Serializable;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import com.karine.go4lunch.utils.FirebaseUtils;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.views.RestaurantAdapter;
import com.muddzdev.styleabletoast.StyleableToast;

import static com.karine.go4lunch.utils.DatesAndHours.getCurrentTime;

public class RestaurantActivity extends AppCompatActivity implements Serializable {

    private static final int REQUEST_CALL = 100;
    @BindView(R.id.resto_photo)
    ImageView mRestoPhoto;
    @BindView(R.id.resto_name)
    TextView mRestoName;
    @BindView(R.id.rating_bar)
    RatingBar mRating;
    @BindView(R.id.resto_address)
    TextView mRestoAddress;
    @BindView(R.id.call_btn)
    Button mCallBtn;
    @BindView(R.id.web_btn)
    Button mWebBtn;
    @BindView(R.id.floating_ok_btn)
    FloatingActionButton mFloatingBtn;
    @BindView(R.id.restaurant_RV)
    RecyclerView mRecyclerViewRestaurant;
    @BindView(R.id.star_btn)
    Button mStarBtn;

    String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;


    private String formattedPhoneNumber;
    private RequestManager mGlide;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = db.collection("users");
    private RestaurantAdapter restaurantAdapter;
    private Disposable mDisposable;
    private static final String SELECTED = "SELECTED";
    private static final String UNSELECTED = "UNSELECTED";
    private String placeId;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);

        this.retrieveData();
        this.floatingBtn();
        this.starBtn();
        this.setUpRecyclerView(placeId);

        //For retrieve data when activity is open
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        //for retrieve like when open
        PlaceDetailsResult placeDetailsResult = null;
        if (bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            final String placeRestaurantId = placeDetailsResult.getPlaceId();
            UserHelper.getUser(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        if (user.getLike() != null && !user.getLike().isEmpty() && user.getLike().contains(placeRestaurantId)) {
                            mStarBtn.setBackgroundColor(Color.BLUE);
                        } else {
                            mStarBtn.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }
            });
        }

        //For Hide Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * For retrieve data to ListFragment
     */
    private void retrieveData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;

        if (bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            updateUI(placeDetailsResult, mGlide);
            placeId = placeDetailsResult.getPlaceId();

        }

    }

    /**
     * For update UI
     *
     * @param placeDetailsResult
     * @param glide
     */
    private void updateUI(PlaceDetailsResult placeDetailsResult, RequestManager glide) {
        mGlide = glide;

        //for add photos with Glide
        if (placeDetailsResult.getPhotos() != null && !placeDetailsResult.getPhotos().isEmpty()) {
            Glide.with(this)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + placeDetailsResult.getPhotos().get(0).getPhotoReference() + "&key=" + GOOGLE_MAP_API_KEY)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mRestoPhoto);
        } else {
            mRestoPhoto.setImageResource(R.drawable.no_picture);
        }
        //For Restaurant Name
        mRestoName.setText(placeDetailsResult.getName());
        //For Restaurant address
        mRestoAddress.setText(placeDetailsResult.getVicinity());
        //For rating
        restaurantRating(placeDetailsResult);
        //For  restaurant telephone number
        String formattedPhoneNumber = placeDetailsResult.getFormattedPhoneNumber();
        callBtn(formattedPhoneNumber);
        //For Website
        String url = placeDetailsResult.getWebsite();
        webBtn(url);
    }



    private void restaurantRating(PlaceDetailsResult placeDetailsResult) {

        if (placeDetailsResult.getRating() != null) {
            double restaurantRating = placeDetailsResult.getRating();
            double rating = (restaurantRating / 5) * 3;
            this.mRating.setRating((float) rating);
            this.mRating.setVisibility(View.VISIBLE);

        } else {
            this.mRating.setVisibility(View.GONE);
        }
    }
    /**
     * For floating button
     */
    public void floatingBtn() {
        mFloatingBtn.setOnClickListener(v -> {
            if (v.getId() == R.id.floating_ok_btn)
                if (SELECTED.equals(mFloatingBtn.getTag())) {
                    selectedRestaurant();

                } else if (mFloatingBtn.isSelected()) {
                    selectedRestaurant();

                } else {
                    removeRestaurant();
                }
        });
    }

    /**
     * For retrieve selected restaurant
     */
    public void selectedRestaurant() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;
        if (bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }

        if (placeDetailsResult != null) {
            UserHelper.updatePlaceId(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getUid(), placeDetailsResult.getPlaceId(), getCurrentTime());
            mFloatingBtn.setImageDrawable(getResources().getDrawable(R.drawable.fui_ic_check_circle_black_128dp));
            mFloatingBtn.setTag(UNSELECTED);
        }
    }

    /**
     * For remove restaurant choice
     */
    public void removeRestaurant() {
        UserHelper.deletePlaceId(Objects.requireNonNull(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getUid()));
        mFloatingBtn.setImageDrawable(getResources().getDrawable(R.drawable.baseline_done_white_24));
        mFloatingBtn.setTag(SELECTED);
    }


    /**
     * For click call button
     * @param formattedPhoneNumber
     */
    public void callBtn(String formattedPhoneNumber) {
        mCallBtn.setOnClickListener(view -> makePhoneCall(formattedPhoneNumber));
    }

    /**
     * For call and permission
     * @param formattedPhoneNumber
     */
    private void makePhoneCall(String formattedPhoneNumber) {

        if (ContextCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RestaurantActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else if (formattedPhoneNumber != null && !formattedPhoneNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + formattedPhoneNumber));
            Log.d("PhoneNumber", formattedPhoneNumber);
            startActivity(intent);
        } else {
            StyleableToast.makeText(RestaurantActivity.this, getString(R.string.no_phone_available), R.style.personalizedToast).show();
        }
    }

    /**
     * For permissions call
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(formattedPhoneNumber);
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * For click website button
     * @param url
     */
    public void webBtn(String url) {
        mWebBtn.setOnClickListener(view -> makeWebView(url));
    }

    /**
     * For webview
     * @param url
     */
    public void makeWebView(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(RestaurantActivity.this, WebViewActivity.class);
            intent.putExtra("website", url);
            Log.d("Website", url);
            startActivity(intent);
        } else {
            StyleableToast.makeText(this, getString(R.string.no_website), R.style.personalizedToast).show();
        }
    }

    /**
     * RecyclerView configuration Workmates
     * @param placeId
     */
    private void setUpRecyclerView(String placeId) {


        Query query = collectionUsers.whereEqualTo("placeId", placeId);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        this.restaurantAdapter = new RestaurantAdapter(options, Glide.with(this));
        mRecyclerViewRestaurant.setHasFixedSize(true);
        mRecyclerViewRestaurant.setAdapter(restaurantAdapter);
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        restaurantAdapter.stopListening();
    }

    /**
     * dispose subscription
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * dispose subscription
     */
    private void disposeWhenDestroy() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    /**
     * For like button
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void starBtn() {
        mStarBtn.setOnClickListener(v ->
                likeRestaurant());
    }

    /**
     * For like/dislike
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void likeRestaurant() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        PlaceDetailsResult placeDetailsResult = null;
        if (bundle != null) {
            placeDetailsResult = (PlaceDetailsResult) bundle.getSerializable("placeDetailsResult");
        }
        if (placeDetailsResult != null) {
            final String placeRestaurantId = placeDetailsResult.getPlaceId();
            UserHelper.getUser(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getUid()).addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    if (!user.getLike().isEmpty() && user.getLike().contains(placeRestaurantId)) {
                        UserHelper.deleteLike(FirebaseUtils.getCurrentUser().getUid(), placeRestaurantId);
                        mStarBtn.setBackgroundResource(R.color.fui_transparent);
                    } else {
                        UserHelper.updateLike(FirebaseUtils.getCurrentUser().getUid(), placeRestaurantId);
                        mStarBtn.setBackgroundResource(R.color.quantum_yellow);
                    }
                }
            });
        }
    }
}

