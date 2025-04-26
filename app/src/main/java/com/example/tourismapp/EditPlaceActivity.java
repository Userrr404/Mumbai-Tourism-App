package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditPlaceActivity extends AppCompatActivity {
    ImageView editPlaceImage;

    EditText editName, editDescription, editCategory, editTags, editExactLocation, editTiming, editFees, editContact, editLocation;
    Button btnUpdate;
    String placeId; // from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        editPlaceImage = findViewById(R.id.editPlaceImage);
        editName = findViewById(R.id.editPlaceName);
        editDescription = findViewById(R.id.editDescription);
        editCategory = findViewById(R.id.editCategory);
        editTags = findViewById(R.id.editTags);
        editExactLocation = findViewById(R.id.editExactLocation);
        editTiming = findViewById(R.id.editTiming);
        editFees = findViewById(R.id.editFees);
        editContact = findViewById(R.id.editContact);
        editLocation = findViewById(R.id.editLocation);
        btnUpdate = findViewById(R.id.btnUpdatePlace);

        Intent intent = getIntent();
        if (intent != null) {
            placeId = intent.getStringExtra("place_id");
            String imageUrl = intent.getStringExtra("image_url");
            String placeName = intent.getStringExtra("place_name");
            String description = intent.getStringExtra("description");
            String category = intent.getStringExtra("category");
            String tags = intent.getStringExtra("tags");
            String exactLocation = intent.getStringExtra("exact_location");
            String timing = intent.getStringExtra("timing");
            String fees = intent.getStringExtra("fees");
            String contact = intent.getStringExtra("contact");
            String location = intent.getStringExtra("location");

            editName.setText(placeName);
            editDescription.setText(description);
            editCategory.setText(category);
            editTags.setText(tags);
            editExactLocation.setText(exactLocation);
            editTiming.setText(timing);
            editFees.setText(fees);
            editContact.setText(contact);
            editLocation.setText(location);

            // Load image (using Glide library)
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_person_img) // fallback
                    .into(editPlaceImage);
        }

        btnUpdate.setOnClickListener(v -> updatePlace());
    }

    private void updatePlace() {
        String placeName = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String tags = editTags.getText().toString().trim();
        String exactLocation = editExactLocation.getText().toString().trim();
        String timing = editTiming.getText().toString().trim();
        String fees = editFees.getText().toString().trim();
        String contact = editContact.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        // Validate inputs to avoid null or empty values
        if (placeName.isEmpty() || description.isEmpty() || category.isEmpty() || tags.isEmpty() ||
                exactLocation.isEmpty() || timing.isEmpty() || fees.isEmpty() || contact.isEmpty() || location.isEmpty()) {
            Toast.makeText(EditPlaceActivity.this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return; // Stop the update process if validation fails
        }

        // Ensure that placeId is not null or empty
        if (placeId == null || placeId.isEmpty()) {
            Toast.makeText(EditPlaceActivity.this, "Place ID is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("place_id", placeId) // Pass the place ID you want to update
                .add("name", placeName)
                .add("description", description)
                .add("category", category)
                .add("tags", tags)
                .add("exact_location", exactLocation)
                .add("timing", timing)
                .add("fees", fees)
                .add("contact", contact)
                .add("location", location)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.0.101/tourism/admin_api/db_update_place.php") // your PHP API
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(EditPlaceActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditPlaceActivity.this, "Place Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(EditPlaceActivity.this, "Update failed", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
