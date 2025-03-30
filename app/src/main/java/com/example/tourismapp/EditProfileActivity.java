package com.example.tourismapp;

import static com.example.tourismapp.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditProfileActivity extends AppCompatActivity {

    TextView txtUserIDProfile;
    EditText editFullNameProfile, editUsernameProfile;
    Button btnSaveProfile;

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
        setContentView(R.layout.activity_edit_profile);

        txtUserIDProfile = findViewById(R.id.txtUserIDProfile);
        editFullNameProfile = findViewById(id.editFullNameProfile);
        editUsernameProfile = findViewById(id.editUsernameProfile);
        btnSaveProfile = findViewById(id.btnSaveProfile);

        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id","N/A");
        String username = sharedPreferences.getString("username","N/A");
        String fullName = sharedPreferences.getString("fullName","User NO: "+userId);
        txtUserIDProfile.setText(userId);
        editUsernameProfile.setText(username);
        editFullNameProfile.setText(fullName);


        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USERID = txtUserIDProfile.getText().toString();
                String FULLNAME = editFullNameProfile.getText().toString();
                String USERNAME = editUsernameProfile.getText().toString();

                if(USERID.isEmpty() || USERNAME.isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "User ID and Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                String URL = "http://192.168.0.100/tourism/db_updateProfile_pro.php";
                RequestBody formBody = new FormBody.Builder()
                        .add("id",USERID)
                        .add("username",USERNAME)
                        .add("full_name",FULLNAME)
                        .build();

                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() ->{
                            Toast.makeText(EditProfileActivity.this,"Error: " +e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        assert response.body() != null;
                        String responseData = response.body().string();

                        runOnUiThread(() ->{
                            try{
                                JSONObject jsonObject = new JSONObject(responseData);
                                String status = jsonObject.getString("status");

                                if(status.equals("success")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username",editUsernameProfile.getText().toString().trim());
                                    editor.putString("fullName",editFullNameProfile.getText().toString().trim());
                                    editor.apply();

                                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();


//                                    Intent iProfileFragment = new Intent(EditProfileActivity.this,ProfileFragment.class);
//                                    startActivity(iProfileFragment);
                                 /*

                                    ProfileFragment is a Fragment, not an Activity. You cannot start a fragment using an Intent.
                                    Instead, you need to finish EditProfileActivity so that when you return, ProfileFragment is refreshed automatically via onResume().
                                */
                                    finish();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
//                            throw new RuntimeException(e);
                                Toast.makeText(EditProfileActivity.this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

}