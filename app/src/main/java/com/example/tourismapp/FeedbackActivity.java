package com.example.tourismapp;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismapp.FeedbackAdapter;
import com.example.tourismapp.Feedback;
import com.example.tourismapp.Utills.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackActivity extends AppCompatActivity {
    TextView textAdminInfo;
    ImageView backButton;

    private ListView listViewFeedback;
    private FeedbackAdapter feedbackAdapter;
    private ArrayList<Feedback> feedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_feedback);
        textAdminInfo = findViewById(R.id.adminEmailText);
        backButton = findViewById(R.id.backButton);

        listViewFeedback = findViewById(R.id.listViewFeedback);

        // Initialize feedback list
        feedbackList = new ArrayList<>();

        // Get admin email from intent
        String adminEmail = getIntent().getStringExtra("adminEmail");
        if (adminEmail != null) {
            textAdminInfo.setText("Logged in: " + adminEmail);
        }

// Handle back button
        backButton.setOnClickListener(v -> onBackPressed());

        // Fetch feedback data from the database
        fetchFeedbackData();
    }

    private void fetchFeedbackData() {
        // Create OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Create the GET request
        Request request = new Request.Builder()
                .url(ApiClient.VIEW_ALL_FEEDBACK) // Replace with your server URL
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(FeedbackActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse the JSON response
                        String jsonResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        // Clear the current feedback list
                        feedbackList.clear();

                        // Loop through the JSON array and create Feedback objects
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject feedbackObject = jsonArray.getJSONObject(i);
                            Feedback feedback = new Feedback();
                            feedback.setId(feedbackObject.getInt("id"));
                            feedback.setUser_id(feedbackObject.getInt("user_id"));
                            feedback.setUsername(feedbackObject.getString("username"));
                            feedback.setUser_feed(feedbackObject.getString("user_feed"));
                            feedback.setCreated_At(feedbackObject.getString("created_at"));

                            // Add feedback to the list
                            feedbackList.add(feedback);
                        }

                        // Update the ListView on the main thread
                        runOnUiThread(() -> {
                            feedbackAdapter = new FeedbackAdapter(FeedbackActivity.this, feedbackList);
                            listViewFeedback.setAdapter(feedbackAdapter);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(FeedbackActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(FeedbackActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
