package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // label hide
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);

        // FOR BOTTOM NAVIGATION SELECT
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_explore){
                    loadFragment(new ExploreFragment(),false);
                } else if (id == R.id.nav_itinerary) {
                    loadFragment(new ItineraryFragment(),false);
                } else if (id == R.id.nav_events) {
                    loadFragment(new EventsFragment(),false);
                } else if (id == R.id.nav_saved) {
                    loadFragment(new SaveFragment(),false);
                } else{ // PROFILE
                    loadFragment(new ProfileFragment(),true);
                }


                // TO CHANGE SELECTION OF FRAGMENT
                return true;
            }
        });
        // BY-DEFAULT OPEN FRAGMENT
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    public void loadFragment(Fragment fragment, boolean flag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(flag){
            fragmentTransaction.add(R.id.fragmentContainer,fragment);
        }else{
            fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        }
        fragmentTransaction.commit();
    }
}