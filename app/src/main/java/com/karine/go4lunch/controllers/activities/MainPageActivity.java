package com.karine.go4lunch.controllers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karine.go4lunch.R;
import com.karine.go4lunch.controllers.fragments.ChatFragment;
import com.karine.go4lunch.controllers.fragments.ListFragment;
import com.karine.go4lunch.controllers.fragments.MapFragment;
import com.karine.go4lunch.controllers.fragments.WorkMatesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
        configureToolbar();
        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);
    }
        @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //2 inflate the menu and add it to the toolbar
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }
    private void configureToolbar(){
        //get the toolbar view inside the activity layout
        setSupportActionBar(toolbar);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout,
                            selectedFragment).commit();
                    return true;
                }
            };
}
