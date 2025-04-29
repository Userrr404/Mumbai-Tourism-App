// AddPlaceActivity.java
package com.example.tourismapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tourismapp.Utills.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPlaceActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, categoryEditText, tagsEditText, exact_locationEditText, timingEditText, feesEditText, contactEditText, locationEditText;
    private Button saveButton;
    private ImageView placeImageView;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_place);

        placeImageView = findViewById(R.id.placeImageView);
        saveButton = findViewById(R.id.saveButton);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
        exact_locationEditText = findViewById(R.id.exact_locationEditText);
        timingEditText = findViewById(R.id.timingEditText);
        feesEditText = findViewById(R.id.feesEditText);
        contactEditText = findViewById(R.id.contactEditText);
        locationEditText = findViewById(R.id.locationEditText);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                placeImageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        placeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    uploadData();
                } else {
                    Toast.makeText(getApplicationContext(), "Select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        final String name = nameEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        final String category = categoryEditText.getText().toString().trim();
        final String tags = tagsEditText.getText().toString().trim();
        final String exact_location = exact_locationEditText.getText().toString().trim();
        final String timing = timingEditText.getText().toString().trim();
        final String fees = feesEditText.getText().toString().trim();
        final String contact = contactEditText.getText().toString().trim();
        final String location = locationEditText.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = ApiClient.ADD_PLACE_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(AddPlaceActivity.this, "Place added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddPlaceActivity.this, "Failed: " + response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPlaceActivity.this, "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", base64Image);
                params.put("name", name);
                params.put("description", description);
                params.put("category", category);
                params.put("tags", tags);
                params.put("exact_location", exact_location);
                params.put("timing", timing);
                params.put("fees", fees);
                params.put("contact", contact);
                params.put("location", location);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
