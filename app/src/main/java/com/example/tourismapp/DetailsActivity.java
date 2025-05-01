package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourismapp.Utills.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView imageViewDetail;
    TextView txtViewId, txtViewName, txtViewInfo, txtViewCategory,txtViewTags, txtViewLocation, txtViewTiming, txtViewFees, txtViewContact;
    Button bookingButton;
    private EditText feedbackEditText;
    private Button submitFeedbackButton;

    String imageUrl, id, name, info; // Declare globally to access inside button click
    String loggedUserId,loggedUserEmail;

    private GoogleMap map;

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
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewDetail = findViewById(R.id.imageViewDetail);
        txtViewId = findViewById(R.id.txtViewId);
        txtViewName = findViewById(R.id.txtViewName);
        txtViewInfo = findViewById(R.id.txtViewInfo);
        txtViewCategory = findViewById(R.id.txtViewCategory);
        txtViewTags = findViewById(R.id.txtViewTags);
        txtViewLocation = findViewById(R.id.txtViewLocation);
        txtViewTiming = findViewById(R.id.txtViewTiming);
        txtViewFees = findViewById(R.id.txtViewFees);
        txtViewContact = findViewById(R.id.txtViewContact);

        // Booking
        bookingButton = findViewById(R.id.btnBooking);

        // Find views
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        // Get username from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        loggedUserId = preferences.getString("user_id", null);
        loggedUserEmail = preferences.getString("user_email",null);

        if (loggedUserId == null && loggedUserEmail == null) {
            Toast.makeText(DetailsActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("urlImage");
        id = intent.getStringExtra("place_id");
        name = intent.getStringExtra("name");
        info = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");
        String tags = intent.getStringExtra("tags");
        String location = intent.getStringExtra("exact_location");
        String timing = intent.getStringExtra("timing");
        String fees = intent.getStringExtra("fees");
        String contact = intent.getStringExtra("contact");
        String latStr = intent.getStringExtra("latitude");
        String lngStr = intent.getStringExtra("longitude");


        txtViewId.setText(id);
        txtViewName.setText(name);
        txtViewInfo.setText(info);
        txtViewCategory.setText("Category: "+category);
        txtViewTags.setText("Tags: "+tags);
        txtViewLocation.setText("Location: "+location);
        txtViewTiming.setText("Time: "+timing);
        txtViewFees.setText("Fees: "+fees);
        txtViewContact.setText("Contact: "+contact);


        Glide.with(this).load(imageUrl).into(imageViewDetail);

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBook = new Intent(DetailsActivity.this, BookingActivity.class);

                iBook.putExtra("place_id",id);
                iBook.putExtra("image_path",imageUrl);
                iBook.putExtra("name",name);
                iBook.putExtra("description",info);
                iBook.putExtra("user_id",loggedUserId);
                iBook.putExtra("user_email",loggedUserEmail);
                startActivity(iBook);
            }
        });

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        // Google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        Intent intent = getIntent();
        String latStr = intent.getStringExtra("latitude");
        String lngStr = intent.getStringExtra("longitude");

        if(latStr != null && lngStr != null){
            double latitude = Double.parseDouble(latStr);
            double longitude = Double.parseDouble(lngStr);

            LatLng placeLocation = new LatLng(latitude,longitude);
            map.addMarker(new MarkerOptions().position(placeLocation).title(name));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));
        }else{
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitFeedback() {
        String feedback = feedbackEditText.getText().toString().trim();
        if (feedback.isEmpty()) {
            Toast.makeText(DetailsActivity.this, "Please enter feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get username from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        String username = preferences.getString("username", null);

        if (username == null) {
            Toast.makeText(DetailsActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }


        // Create the request to send the feedback to the server
        OkHttpClient client = new OkHttpClient();
        String url = ApiClient.SUBMIT_FEEDBACK; // Your server URL

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("user_feed", feedback)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Feedback submitted successfully
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            feedbackEditText.setText(""); // Clear the input field
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}