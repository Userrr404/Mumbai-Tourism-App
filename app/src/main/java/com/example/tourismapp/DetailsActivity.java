package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageViewDetail;
//    TextView txtViewName, txtViewInfo, txtViewId;
    TextView txtViewId, txtViewName, txtViewInfo, txtViewCategory,txtViewTags, txtViewLocation, txtViewTiming, txtViewFees, txtViewContact;
    private EditText feedbackEditText;
    private Button submitFeedbackButton;

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
//        txtViewName = findViewById(R.id.txtViewName);
//        txtViewInfo = findViewById(R.id.txtViewInfo);
//        txtViewId = findViewById(R.id.txtViewId);
//
//        Intent intent = getIntent();
//        String imageUrl = intent.getStringExtra("urlImage");
//        String name = intent.getStringExtra("name");
//        String info = intent.getStringExtra("info");
//        String id = intent.getStringExtra("id");
//
//        txtViewName.setText(name);
//        txtViewInfo.setText(info);
//        txtViewId.setText(id);

        txtViewId = findViewById(R.id.txtViewId);
        txtViewName = findViewById(R.id.txtViewName);
        txtViewInfo = findViewById(R.id.txtViewInfo);
        txtViewCategory = findViewById(R.id.txtViewCategory);
        txtViewTags = findViewById(R.id.txtViewTags);
        txtViewLocation = findViewById(R.id.txtViewLocation);
        txtViewTiming = findViewById(R.id.txtViewTiming);
        txtViewFees = findViewById(R.id.txtViewFees);
        txtViewContact = findViewById(R.id.txtViewContact);
        // Find views
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("urlImage");
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String info = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");
        String tags = intent.getStringExtra("tags");
        String location = intent.getStringExtra("exact_location");
        String timing = intent.getStringExtra("timing");
        String fees = intent.getStringExtra("fees");
        String contact = intent.getStringExtra("contact");

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
        String url = "http://192.168.0.101/tourism/db_submit_feedback.php"; // Your server URL

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