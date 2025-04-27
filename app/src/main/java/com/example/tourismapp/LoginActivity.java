package com.example.tourismapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String BASE_URL = "http://192.168.0.101/tourism/db_verify_login.php";

    private final OkHttpClient client = new OkHttpClient();

    EditText editEmailAddLog, editPasswordLog;
    Button btnLoginLog;
    TextView txtRedirectSignupLog;

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
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editEmailAddLog = findViewById(R.id.editEmailAddLog);
        editPasswordLog = findViewById(R.id.editPasswordLog);
        btnLoginLog = findViewById(R.id.btnLoginLog);
        txtRedirectSignupLog = findViewById(R.id.txtRedirectSignupLog);

        txtRedirectSignupLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(iRegister);
            }
        });

        btnLoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });
    }

    public void verifyData(){
        String email = editEmailAddLog.getText().toString();
        String password = editPasswordLog.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginActivity.this, "All fields are required",Toast.LENGTH_SHORT).show();
            return;
        }

        // CREATE REQUEST BODY
        RequestBody formData = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .build();

        // CREATE A REQUEST
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(formData)
                .build();

        // CALL THE REQUEST
        Call call = client.newCall(request);

        // MANAGE THE CALL
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->{
                    Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                });
            }

//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                assert response.body() != null;
//                String responseBody = response.body().string();
//
//                runOnUiThread(() ->{
//                    if(responseBody.equals("success")){
//
//                        // SAVE THE LOGIN STATE
//                        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
//
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putBoolean("flag",true);
////                        editor.putString("email",email);
//                        editor.apply();
//
//                        // START HOME ACTIVITY
//                        Intent iHome = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(iHome);
//
//                    } else if (responseBody.equals("Invalid email or password")) {
//                        // Show invalid login message
//                        Toast.makeText(LoginActivity.this, "Invalid email or Password", Toast.LENGTH_SHORT).show();
//                    } else if (responseBody.equals("Invalid request method")) {
//                        // Handle invalid request method message
//                        Toast.makeText(LoginActivity.this, "Invalid request method", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(!responseBody.contains("Invalid")){
                    try{
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String userId = jsonObject.getString("id");
                        String userEmail = jsonObject.getString("email");
                        String username = jsonObject.getString("username");
//                        String fullName = jsonObject.getString("full_name");

//                        Used optString("full_name", userId) → If full_name is missing, it defaults to "NULL" instead of crashing.
                        String fullName = jsonObject.optString("full_name","").trim();

                        if(fullName.isEmpty()){
                            fullName = userId;  // IF full_name IS EMPTY, USE userId
                        }

                        // SAVE THE LOGIN STATE
                        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id",userId);
                        editor.putString("email",userEmail);
                        editor.putString("username",username);
                        editor.putString("fullName",fullName);
                        editor.putBoolean("isLoggedIn",true);
                        editor.apply();

                        // START THE ACTIVITY
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this,"Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent iHome = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(iHome);
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}