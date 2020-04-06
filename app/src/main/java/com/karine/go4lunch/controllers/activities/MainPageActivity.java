package com.karine.go4lunch.controllers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.FirebaseUtils;
import com.karine.go4lunch.Utils.Go4LunchStream;
import com.karine.go4lunch.controllers.fragments.ChatFragment;
import com.karine.go4lunch.controllers.fragments.ListFragment;
import com.karine.go4lunch.controllers.fragments.MapFragment;
import com.karine.go4lunch.controllers.fragments.WorkMatesFragment;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.models.User;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.karine.go4lunch.Utils.FirebaseUtils.getCurrentUser;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declarations
    @BindView(R.id.main_page_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_page_nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.searchView)
    MaterialSearchView searchView;


    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int SIGN_OUT_TASK = 100;
    private Disposable mDisposable;
    private PlaceDetail detail;
    private User users;
    private String userId;
    private String idResto;
    private PlaceDetailsResult result;
    private String nameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.updateUINavHeader();
        this.searchViewQueryListener();
        this.searchViewListener();

        //For change title Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("I'm Hungry");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);
        //For connect MapFragment with activity
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout,
                new MapFragment()).commit();

    }
    //    Configure toolbar
    private void configureToolbar() {
        setSupportActionBar(toolbar);
    }

    /**
     * Request for sign out
     */

    private void signOutFromUserFirebase() {
        if (FirebaseUtils.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRestRequestsCompleted(SIGN_OUT_TASK));
        }
    }

    //Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRestRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch(origin) {
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }
        @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //2 inflate the menu and add it to the toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        searchView.setMenuItem(item);
        return true;
    }

    //For SearchView Listener
    public void searchViewQueryListener () {

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchViewListener () {
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
    }

    //For back click to close menu
    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //button connection with fragments
    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;


                    switch (menuItem.getItemId()) {
                        case R.id.map_btn:
                            selectedFragment = new MapFragment();

                            break;
                        case R.id.list_btn:
                            selectedFragment = new ListFragment();
                            break;
                        case R.id.workmates_btn:
                            selectedFragment = new WorkMatesFragment();

                            break;
                        case R.id.chat_btn:
                            selectedFragment = new ChatFragment();
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };
    //Handle Navigation Item Click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_drawer_lunch :
                if(FirebaseUtils.getCurrentUser() != null) {
                    UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            if (Objects.requireNonNull(user).getPlaceId() != null) {
                                userResto(user);
                            } else {
                                StyleableToast.makeText(getApplicationContext(), "No restaurant choose for the moment", R.style.personalizedToast).show();
                            }
                        }
                    });
                }
                break;
            case R.id.menu_drawer_settings:
                break;
            case R.id.menu_drawer_Logout:
                signOutFromUserFirebase();
                StyleableToast.makeText(getApplicationContext(),"You're deconnected",R.style.personalizedToast).show();;
                break;

        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.main_page_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    //Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.main_page_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Update UI Nav Header
     */
    private void updateUINavHeader() {
        if (FirebaseUtils.getCurrentUser() != null) {
            View headerView = mNavigationView.getHeaderView(0); //For return layout
            ImageView mPhotoHeader = headerView.findViewById(R.id.photo_header);
            TextView mNameHeader = headerView.findViewById(R.id.name_header);
            TextView mMailHeader = headerView.findViewById(R.id.mail_header);
            // get photo in Firebase
            if (FirebaseUtils.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(FirebaseUtils.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mPhotoHeader);
            } else {
                mPhotoHeader.setImageResource(R.drawable.no_picture);
            }
            //Get email
            String email = TextUtils.isEmpty(FirebaseUtils.getCurrentUser().getEmail()) ?
                    ("No Email Found") : FirebaseUtils.getCurrentUser().getEmail();
            //Get Name
            String name = TextUtils.isEmpty(FirebaseUtils.getCurrentUser().getDisplayName()) ?
                    ("No Username Found") : FirebaseUtils.getCurrentUser().getDisplayName();
            //Update With data
            mNameHeader.setText(name);
            mMailHeader.setText(email);

        }
    }

    private void userResto (User users) {
       idResto = users.getPlaceId();
        executeHttpRequestWithRetrofit();
    }


    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = Go4LunchStream.streamFetchDetails(idResto)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {

                    @Override
                    public void onNext(PlaceDetail placeDetail) {

                        detail = placeDetail;

                        startForLunch();

                    }
                    @Override
                    public void onComplete() {
                        if(idResto != null) {
                            Log.d("your lunch request", "your lunch" + detail.getResult());
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErrorYourLunc", Log.getStackTraceString(e));
                    }
                });
    }
    public void startForLunch() {
        Intent intent = new Intent(this, RestaurantActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("placeDetailsResult", detail.getResult());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

}
