package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingActivity extends AppCompatActivity {

    ImageView imageViewBooking;
    TextView txtPlaceName, txtPlaceDescription, txtUserId, txtUserEmail, txtPlaceId;
    Button btnConfirmBooking;

    String placeId, imagePath, placeName, placeDescription, userId, userEmail;
    TextView feesTextView;

    EditText editNumberOfPeople, editBookingDate;

    int totalFees = 200;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_booking); // Ensure the correct XML layout file is used

        // Initialize views
        imageViewBooking = findViewById(R.id.imageViewBooking);
        txtPlaceName = findViewById(R.id.txtPlaceName);
        txtPlaceDescription = findViewById(R.id.txtPlaceDescription);
        // Reference for the new TextView
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        // Retrieve data from Intent
        placeId = getIntent().getStringExtra("place_id");
        imagePath = getIntent().getStringExtra("image_path");
        placeName = getIntent().getStringExtra("name");
        placeDescription = getIntent().getStringExtra("description");
        userId = getIntent().getStringExtra("user_id");
        userEmail = getIntent().getStringExtra("email");

        txtPlaceName.setText(placeName);
        txtPlaceDescription.setText(placeDescription);
//        txtPlaceId.setText(placeId);

        feesTextView = findViewById(R.id.feesTextView);
        feesTextView.setText("Fees: ₹500");


        editNumberOfPeople = findViewById(R.id.editNumberOfPeople);
        editBookingDate = findViewById(R.id.editBookingDate);

        // Set default number of people to 1
        editNumberOfPeople.setText("1");

        // Set initial fee
        updateFees();

        // Listen for changes in number of people
        editNumberOfPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update fees when text changes
                updateFees();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        // Load the image using Glide
        Glide.with(this).load(imagePath).into(imageViewBooking);

        // Handle confirm booking button click
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBookingToServer();
            }
        });
    }

    private void updateFees() {
        String numberStr = editNumberOfPeople.getText().toString().trim();
        int numberOfPeople = 1; // Default to 1 if empty

        if (!numberStr.isEmpty()) {
            try {
                numberOfPeople = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                numberOfPeople = 1;
            }
        }

        int feePerPerson = 200;
        totalFees = feePerPerson * numberOfPeople; // Now we update the global totalFees
        feesTextView.setText("Fees: ₹" + totalFees);
    }


    private void sendBookingToServer() {
        if (placeId == null || imagePath == null || placeName == null || placeDescription == null || userId == null || userEmail == null) {
            Toast.makeText(this, "Some booking details are missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        String numberOfPeople = editNumberOfPeople.getText().toString().trim();
        String bookingDate = editBookingDate.getText().toString().trim();

        if (numberOfPeople.isEmpty() || bookingDate.isEmpty()) {
            Toast.makeText(this, "Please enter number of people and booking date", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.0.101/tourism/db_insert_booking.php";

        RequestBody formBody = new FormBody.Builder()
                .add("place_id", placeId)
                .add("image_path", imagePath)
                .add("name", placeName)
                .add("description", placeDescription)
                .add("user_id", userId)
                .add("email", userEmail)
                .add("number_of_people", numberOfPeople)    // NEW FIELD
                .add("booking_date", bookingDate)
                .add("fees", String.valueOf(totalFees)) // NEW FIELD
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(BookingActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookingActivity.this, "Booking successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BookingActivity.this, "Booking failed: " + responseBody, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}