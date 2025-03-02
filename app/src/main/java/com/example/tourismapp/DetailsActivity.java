package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageViewDetail;
//    TextView txtViewName, txtViewInfo, txtViewId;
    TextView txtViewId, txtViewName, txtViewInfo, txtViewCategory,txtViewTags, txtViewLocation, txtViewTiming, txtViewFees, txtViewContact;

    @SuppressLint("MissingInflatedId")
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
}