package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class ViewPlacesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PlacesAdapter placesAdapter;
    List<Place> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_view_places);

        recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeList = new ArrayList<>();
        placesAdapter = new PlacesAdapter(ViewPlacesActivity.this,placeList);
        recyclerView.setAdapter(placesAdapter);


        fetchPlaces();


    }

    private void fetchPlaces() {
        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.0.101/tourism/admin_api/fetch_places.php"; // Replace with your actual PHP URL

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Failed to load places", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    runOnUiThread(() -> {
                        try {
                            JSONArray jsonArray = new JSONArray(responseData);

                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject placeObject = jsonArray.getJSONObject(i);
////
////                                String id = placeObject.getString("place_id");
////                                String name = placeObject.getString("name");
////                                String imageUrl = placeObject.getString("image_path"); // Assuming you send image URL or name
////
////                                String urlImage = "http://192.168.0.100/tourism/"+imageUrl;
////
////                                    // Now you can create a Place model and add to your list
//////                                Place place = new Place(id, name, urlImage);
////                                Place place = new Place();
////                                place.setPlaceId(id);
////                                place.setName(name);
////                                place.setImagePath(urlImage);
////                                placeList.add(place);

                                JSONObject placeObject = jsonArray.getJSONObject(i);

                                String id = placeObject.getString("place_id");
                                String name = placeObject.getString("name");
                                String imageUrl = placeObject.getString("image_path");
                                String description = placeObject.getString("description");
                                String category = placeObject.getString("category");
                                String tags = placeObject.getString("tags");
                                String exactLocation = placeObject.getString("exact_location");
                                String timing = placeObject.getString("timing");
                                String fees = placeObject.getString("fees");
                                String contact = placeObject.getString("contact");
                                String location = placeObject.getString("location");

                                String urlImage = "http://192.168.0.100/tourism/" + imageUrl;

                                Place place = new Place();
                                place.setPlaceId(id);
                                place.setName(name);
                                place.setImagePath(urlImage);
                                place.setDescription(description);
                                place.setCategory(category);
                                place.setTags(tags);
                                place.setExactLocation(exactLocation);
                                place.setTiming(timing);
                                place.setFees(fees);
                                place.setContact(contact);
                                place.setLocation(location);

                                placeList.add(place);
                            }

                            // Notify your adapter that data is changed
                            placesAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
