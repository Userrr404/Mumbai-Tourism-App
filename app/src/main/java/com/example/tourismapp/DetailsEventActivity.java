package com.example.tourismapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailsEventActivity extends AppCompatActivity {

    EditText editEventName, editEventDate, editEventDesc;
    Button btnSaveEvent;
    ImageView editEventImage;
    Button btnChooseImage;
    Uri selectedImageUri;
    String imageUrl;
    int PICK_IMAGE_REQUEST = 1;

    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_details_event);

        editEventName = findViewById(R.id.editEventName);
        editEventDate = findViewById(R.id.editEventDate);
        editEventDesc = findViewById(R.id.editEventDesc);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("event_id");
        editEventName.setText(intent.getStringExtra("event_name"));
        editEventDate.setText(intent.getStringExtra("event_date"));
        editEventDesc.setText(intent.getStringExtra("event_desc"));
        editEventImage = findViewById(R.id.editEventImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);

        imageUrl = getIntent().getStringExtra("event_image");
        Glide.with(this).load(imageUrl).into(editEventImage); // Load existing image

        btnChooseImage.setOnClickListener(v -> {
            Intent iImage = new Intent();
            iImage.setType("image/*");
            iImage.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(iImage, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        btnSaveEvent.setOnClickListener(v -> {
            updateEventToDatabase(eventId,
                    editEventName.getText().toString(),
                    editEventDate.getText().toString(),
                    editEventDesc.getText().toString());
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            editEventImage.setImageURI(selectedImageUri);
        }
    }

    private void updateEventToDatabase(String id, String name, String date, String desc) {
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("name", name)
                .addFormDataPart("date", date)
                .addFormDataPart("desc", desc);

        if (selectedImageUri != null) {
            String filePath = getRealPathFromURI(selectedImageUri);
            File file = new File(filePath);
            builder.addFormDataPart("image", file.getName(),
                    RequestBody.create(file, MediaType.parse("image/*")));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/tourism/db_update_event.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) { e.printStackTrace(); }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(DetailsEventActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}
