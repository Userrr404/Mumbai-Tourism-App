package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    TextView txtViewName, txtViewInfo, txtViewId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewDetail = findViewById(R.id.imageViewDetail);
        txtViewName = findViewById(R.id.txtViewName);
        txtViewInfo = findViewById(R.id.txtViewInfo);
        txtViewId = findViewById(R.id.txtViewId);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("urlImage");
        String name = intent.getStringExtra("name");
        String info = intent.getStringExtra("info");
        String id = intent.getStringExtra("id");

        txtViewName.setText(name);
        txtViewInfo.setText(info);
        txtViewId.setText(id);

        Glide.with(this).load(imageUrl).into(imageViewDetail);
    }
}