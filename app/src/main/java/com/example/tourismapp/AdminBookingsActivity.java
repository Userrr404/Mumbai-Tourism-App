package com.example.tourismapp;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminBookingsActivity extends AppCompatActivity {
    TextView adminEmailText;
    RecyclerView recyclerView;
    List<Booking> bookingList = new ArrayList<>();
    BookingAdapter adapter;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_admin_bookings);
        adminEmailText = findViewById(R.id.adminEmailText);
        backButton = findViewById(R.id.backButton);

        // Get admin name from Intent
        String adminEmail = getIntent().getStringExtra("adminEmail");
        if (adminEmail != null) {
            adminEmailText.setText("Logged in: " + adminEmail);
        }

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingAdapter(this, bookingList);
        recyclerView.setAdapter(adapter);

        // Back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchBookings();
    }

    private void fetchBookings() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ApiClient.VIEW_ALL_BOOKING)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            Booking booking = new Booking();
                            booking.id = obj.getInt("id");
                            booking.place_id = obj.getInt("place_id");
                            booking.image_path = obj.getString("image_path");
                            booking.name = obj.getString("name");
                            booking.description = obj.getString("description");
                            booking.user_id = obj.getInt("user_id");
                            booking.email = obj.getString("user_email");
                            booking.number_of_people = obj.getInt("number_of_people");
                            booking.booking_date = obj.getString("booking_date");
                            booking.fees = obj.getInt("fees");
                            booking.full_name = obj.getString("full_name");
                            booking.mobile_number = obj.getString("mobile_number");
                            booking.booking_status = obj.getString("booking_status");

                            bookingList.add(booking);
                        }

                        runOnUiThread(() -> adapter.notifyDataSetChanged());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
