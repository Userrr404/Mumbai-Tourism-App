package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashboardActivity extends AppCompatActivity {

    TextView textLoggedInUser;
    ImageView userProfileIcon;

    CardView cardViewPlaces;

    Button buttonLogout, buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_dashboard);

        textLoggedInUser = findViewById(R.id.textLoggedInUser);
        userProfileIcon = findViewById(R.id.userProfileIcon);

        CardView cardViewPlaces = findViewById(R.id.cardViewPlaces);

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonSignup = findViewById(R.id.buttonSignup);

        // Retrieve the admin email passed from AdminLoginActivity
        String adminEmail = getIntent().getStringExtra("adminEmail");

        // Display the admin's email (or name if you pass that)
        if (adminEmail != null) {
            textLoggedInUser.setText("Logged in as: " + adminEmail);
        }

        cardViewPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ViewPlacesActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic here
                Toast.makeText(DashboardActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

                // Example: redirect to LoginActivity
                Intent intent = new Intent(DashboardActivity.this, AdminLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle signup navigation
                Intent intent = new Intent(DashboardActivity.this, AdminSignupActivity.class);
                startActivity(intent);
            }
        });
    }
}