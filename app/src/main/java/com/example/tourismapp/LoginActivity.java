package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = ApiClient.USER_LOGIN;

    private final OkHttpClient client = new OkHttpClient();

    private EditText editEmailAddLog, editPasswordLog;
    private Button btnLoginLog;
    private TextView txtRedirectSignupLog;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        editEmailAddLog = findViewById(R.id.editEmailAddLog);
        editPasswordLog = findViewById(R.id.editPasswordLog);
        btnLoginLog = findViewById(R.id.btnLoginLog);
        txtRedirectSignupLog = findViewById(R.id.txtRedirectSignupLog);

        txtRedirectSignupLog.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnLoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false); // user can't dismiss by touching outside

    }

    private void verifyData() {
        // Show Progress Dialog
        String email = editEmailAddLog.getText().toString().trim();
        String password = editPasswordLog.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody formData = new FormBody.Builder()
                .add("user_email", email)
                .add("user_password", password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(formData)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->{
                    progressDialog.dismiss(); // Hide Progress Dialog
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                runOnUiThread(() -> {
                    progressDialog.dismiss(); // Hide Progress Dialog
                    if (responseBody.contains("Invalid Password") || responseBody.contains("User Not Found")) {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            String userId = jsonObject.getString("user_id");
                            String userEmail = jsonObject.getString("user_email");
                            String username = jsonObject.getString("username");
                            String fullName = jsonObject.optString("user_fullName", userId).trim();

                            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("user_id", userId);
                            editor.putString("user_email", userEmail);
                            editor.putString("username", username);
                            editor.putString("user_fullName", fullName);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent iHome = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(iHome);
                            finish();  // Finish LoginActivity after login success

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
