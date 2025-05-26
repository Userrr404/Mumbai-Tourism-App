package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismapp.Utills.ApiClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String BASE_URL = ApiClient.USER_REGISTER;
    private final OkHttpClient client = new OkHttpClient();

    // UI Components
    EditText editUsernameReg, editEmailAddReg, editPasswordReg, editConfirmPassReg;
    Button btnSignupReg;
    TextView txtRedirectLoginReg;
    ProgressBar progressBar;  // ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        // Initializing UI components
        editUsernameReg = findViewById(R.id.editUsernameReg);
        editEmailAddReg = findViewById(R.id.editEmailAddReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        editConfirmPassReg = findViewById(R.id.editConfirmPassReg);
        btnSignupReg = findViewById(R.id.btnRegisterReg);
        txtRedirectLoginReg = findViewById(R.id.clickableRedirectLoginReg);

        // Redirect to Login Activity
        txtRedirectLoginReg.setOnClickListener(v -> {
            Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(iLogin);
        });

        // Insert data in database
        btnSignupReg.setOnClickListener(v -> {
            String username = editUsernameReg.getText().toString();
            String email = editEmailAddReg.getText().toString();
            String password = editPasswordReg.getText().toString();
            String confirmPass = editConfirmPassReg.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are Required", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPass)) {
                    saveData(username, email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to save user data via OkHttp
    private void saveData(String username, String email, String password) {
        // Show ProgressBar
//        progressBar.setVisibility(View.VISIBLE);

        // Create Request Body
        RequestBody formData = new FormBody.Builder()
                .add("username", username)
                .add("user_email", email)
                .add("user_password", password)
                .build();

        // Create Request
        Request request = new Request.Builder().url(BASE_URL).post(formData).build();

        // Call the request
        Call call = client.newCall(request);

        // Manage Call Response
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);  // Hide ProgressBar
                    Toast.makeText(RegisterActivity.this, "Request Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    resetFields();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String resp = response.body().string();

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);  // Hide ProgressBar

                    if (resp.contains("Inserted Successfully")) {
                        Toast.makeText(RegisterActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        resetFields();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else if (resp.contains("Username already in use")) {
                        Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Reset input fields
    private void resetFields() {
        editUsernameReg.setText("");
        editEmailAddReg.setText("");
        editPasswordReg.setText("");
        editConfirmPassReg.setText("");
    }
}
