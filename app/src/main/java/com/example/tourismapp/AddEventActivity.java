package com.example.tourismapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import okhttp3.*;

public class AddEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Bitmap bitmap;

    private EditText editEventName, editEventDate, editEventDescription;
    private ImageView imageViewEvent;
    private Button buttonSelectImage, buttonUploadEvent;

    private static final String UPLOAD_URL = "http://10.0.2.2/tourism/upload_event.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editEventName = findViewById(R.id.editEventName);
        editEventDate = findViewById(R.id.editEventDate);
        editEventDescription = findViewById(R.id.editEventDescription);
        imageViewEvent = findViewById(R.id.imageViewEvent);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonUploadEvent = findViewById(R.id.buttonUploadEvent);

        buttonSelectImage.setOnClickListener(v -> openFileChooser());
        buttonUploadEvent.setOnClickListener(v -> uploadEvent());

        // Inside your onCreate() after findViewById
        editEventDate.setOnClickListener(v -> showDatePickerDialog());


    }

    // Now create the function
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Month is 0-based, so +1
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    editEventDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Event Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewEvent.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadEvent() {
        String name = editEventName.getText().toString().trim();
        String date = editEventDate.getText().toString().trim();
        String description = editEventDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || description.isEmpty() || bitmap == null) {
            Toast.makeText(this, "Please fill all fields and select image", Toast.LENGTH_SHORT).show();
            return;
        }

        String image = imageToString(bitmap);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("date", date)
                .add("description", description)
                .add("image", image)
                .build();

        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(AddEventActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    Toast.makeText(AddEventActivity.this, "Event Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
