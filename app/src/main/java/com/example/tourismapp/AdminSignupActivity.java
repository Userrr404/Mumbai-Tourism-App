package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminSignupActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword,edtConfirmPassword;
    Button btnSignup;

    TextView txtRedirectLoginAdmin;

    OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);

        txtRedirectLoginAdmin = findViewById(R.id.clickableRedirectLoginAdmin);

        txtRedirectLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAdminLogin = new Intent(AdminSignupActivity.this,AdminLoginActivity.class);
                startActivity(iAdminLogin);
            }
        });

        // Insert data in database
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPaas = edtConfirmPassword.getText().toString();

                if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPaas.isEmpty()){
                    Toast.makeText(AdminSignupActivity.this, "All fields are Required", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(confirmPaas)){
                        signupAdmin(username,email,password);
                    }else{
                        Toast.makeText(AdminSignupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void signupAdmin(String username, String email, String password) {

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(ApiClient.ADMIN_SIGNUP_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(AdminSignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show());
                edtUsername.setText("");
                edtEmail.setText("");
                edtPassword.setText("");
                edtConfirmPassword.setText("");
                txtRedirectLoginAdmin.setText("Request Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String resp = response.body().string();


                runOnUiThread(() -> {
                    if(resp.contains("Signup Successful")){
                        Toast.makeText(AdminSignupActivity.this,"Signup Successfully",Toast.LENGTH_SHORT).show();
                        edtUsername.setText("");
                        edtEmail.setText("");
                        edtPassword.setText("");
                        edtConfirmPassword.setText("");

                        Intent iLoginAdmin = new Intent(AdminSignupActivity.this, AdminLoginActivity.class);
                        startActivity(iLoginAdmin);
                    } else if (resp.contains("Email already registered")) {
                        Toast.makeText(AdminSignupActivity.this,"Email is already in use.Choose a different one.",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AdminSignupActivity.this,"Signup Failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
