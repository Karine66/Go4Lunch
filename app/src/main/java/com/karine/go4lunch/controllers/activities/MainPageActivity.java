package com.karine.go4lunch.controllers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.FirebaseUtils;
import com.karine.go4lunch.controllers.fragments.BaseFragment;
import com.karine.go4lunch.controllers.fragments.ChatFragment;
import com.karine.go4lunch.controllers.fragments.ListFragment;
import com.karine.go4lunch.controllers.fragments.MapFragment;
import com.karine.go4lunch.controllers.fragments.WorkMatesFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declarations
    @BindView(R.id.main_page_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int SIGN_OUT_TASK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();

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
//        @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        //2 inflate the menu and add it to the toolbar
//        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
//        return true;
//    }

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
                break;
            case R.id.menu_drawer_settings:
                break;
            case R.id.menu_drawer_Logout:
                signOutFromUserFirebase();
                Toast.makeText(getApplicationContext(),"You're deconnected",Toast.LENGTH_SHORT).show();;
                break;

        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
//    Configure toolbar
    private void configureToolbar() {
        setSupportActionBar(toolbar);
   //    Objects.requireNonNull(getActionBar()).setTitle("I'm Hungry");
    }
    //configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.main_page_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    //Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.main_page_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
