package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminLoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword,txtRedirectSignupAdmin;
    Button btnLogin;

    private OkHttpClient client = new OkHttpClient();

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_admin_login);

        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
//        txtRedirectSignupAdmin = findViewById(R.id.txtRedirectSignupAdmin);
        btnLogin = findViewById(R.id.btnAdminLogin);

//        txtRedirectSignupAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iSignupAdmin = new Intent(AdminLoginActivity.this,AdminSignupActivity.class);
//                startActivity(iSignupAdmin);
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginAdmin(email, password);
                }
            }
        });
    }

    private void loginAdmin(String email, String password) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(ApiClient.ADMIN_LOGIN_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AdminLoginActivity.this.runOnUiThread(() ->
                        Toast.makeText(AdminLoginActivity.this, "Server Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("Response", "Response: " + res);  // Add this line to debug the response body

                AdminLoginActivity.this.runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        if (success) {
                            String adminEmail = jsonObject.getString("email");

                            Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent iAdminDash = new Intent(AdminLoginActivity.this, DashboardActivity.class);
                            iAdminDash.putExtra("adminEmail", adminEmail);
                            startActivity(iAdminDash);
                            finish();
                        } else {
                            Toast.makeText(AdminLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ParsingError", "Error parsing JSON: " + e.getMessage());  // Log detailed error
                        Toast.makeText(AdminLoginActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}