package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingActivity extends AppCompatActivity {
    ImageView imageViewBooking;
    TextView txtPlaceName, txtPlaceDescription;
    Button btnConfirmBooking;
    String placeId, imagePath, placeName, placeDescription, userId, userEmail;
    TextView feesTextView;
    EditText editNumberOfPeople, editBookingDate, editFullName, editMobileNumber;
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
        userEmail = getIntent().getStringExtra("user_email");

        txtPlaceName.setText(placeName);
        txtPlaceDescription.setText(placeDescription);
//        txtPlaceId.setText(placeId);

        feesTextView = findViewById(R.id.feesTextView);
        feesTextView.setText("Fees: ₹500");
        editNumberOfPeople = findViewById(R.id.editNumberOfPeople);
        editBookingDate = findViewById(R.id.editBookingDate);
        editFullName = findViewById(R.id.editFullName);
        editMobileNumber = findViewById(R.id.editMobileNumber);

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
                // Generate 6-digit OTP
                String otp = String.valueOf(new Random().nextInt(900000) + 100000);
                sendOtpToEmail(otp);
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

    private void sendOtpToEmail(String otp) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("user_email", userEmail)
                .add("full_name", editFullName.getText().toString().trim())
                .add("otp", otp)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/tourism/db_sendOtpMail.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Failed to send OTP", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string().trim();
                runOnUiThread(() -> {
                    if (body.equalsIgnoreCase("success")) {
                        showOtpDialog(otp);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to send OTP", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showOtpDialog(String generatedOtp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter OTP");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Verify", (dialog, which) -> {
            String enteredOtp = input.getText().toString().trim();
            if (enteredOtp.equals(generatedOtp)) {
                sendBookingToServer();
            } else {
                Toast.makeText(this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void sendBookingToServer() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("user_id", userId)
                .add("user_email", userEmail)
                .add("place_id",placeId)
                .add("name", placeName)
                .add("image_path", imagePath)
                .add("description", placeDescription)
                .add("number_of_people",editNumberOfPeople.getText().toString().trim())
                .add("booking_date",editBookingDate.getText().toString().trim())
                .add("fees", String.valueOf(totalFees))
                .add("full_name", editFullName.getText().toString().trim())
                .add("mobile_number", editMobileNumber.getText().toString().trim())

                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/tourism/db_insert_booking.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Failed to book", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getBoolean("success")) {
                            Toast.makeText(getApplicationContext(), "Booking confirmed!", Toast.LENGTH_LONG).show();
                            finish(); // close activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Booking failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}