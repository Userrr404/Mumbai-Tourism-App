package com.example.tourismapp;

import android.annotation.SuppressLint;
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

public class EditPasswordActivity extends AppCompatActivity {
    TextView txtUserIDPassProfile;

    EditText editUsernameProProfile, editNewPassProfile, editOldPassProfile;
    Button btnSavePassProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_edit_password);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        txtUserIDPassProfile = findViewById(R.id.txtUserIDPassProfile);
        editUsernameProProfile = findViewById(R.id.editUsernameProProfile);
        editNewPassProfile = findViewById(R.id.editNewPassProfile);
        editOldPassProfile = findViewById(R.id.editOldPassProfile);
        btnSavePassProfile = findViewById(R.id.btnSavePassProfile);

        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String userID = sharedPreferences.getString("user_id","N/A");
        String username = sharedPreferences.getString("username","N/A");

        txtUserIDPassProfile.setText(userID);
        editUsernameProProfile.setText(username);

        btnSavePassProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USERID = txtUserIDPassProfile.getText().toString();
                String USERNAME = editUsernameProProfile.getText().toString();
                String NEWPASS = editNewPassProfile.getText().toString();
                String OLDPASS = editOldPassProfile.getText().toString();

                if(USERNAME.isEmpty() || NEWPASS.isEmpty() || OLDPASS.isEmpty()){
                    Toast.makeText(EditPasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                String URL = ApiClient.UPDATE_USER_PASSWORD;
                RequestBody formBody = new FormBody.Builder()
                        .add("user_id",USERID)
                        .add("username",USERNAME)
                        .add("password",OLDPASS)
                        .add("new_password",NEWPASS)
                        .build();

                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() ->{
                            Toast.makeText(EditPasswordActivity.this,"error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.body() == null) {
                            runOnUiThread(() -> Toast.makeText(EditPasswordActivity.this, "No response from server", Toast.LENGTH_SHORT).show());
                            return;
                        }
                        String responseBody = response.body().string();

                        runOnUiThread(() -> {
                            try{
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String status = jsonObject.getString("status");

                                if(status.equals("success")){
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString("password",NEWPASS);
//                                    editor.apply();

                                    Toast.makeText(EditPasswordActivity.this,"Password Updated successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(EditPasswordActivity.this,"Error " + message,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
                                Toast.makeText(EditPasswordActivity.this,"Response parsing error",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}