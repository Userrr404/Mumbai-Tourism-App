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

public class EditEmailAddActivity extends AppCompatActivity {

    TextView txtUserIdEmailProfile,txtEmailAddProfile;
    EditText editEmailAddProfile,editPasswordEmailProfile;
    Button btnSaveEmailProfile;

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_email_add);
        getSupportActionBar().hide();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        txtUserIdEmailProfile = findViewById(R.id.txtUserIDEmailProfile);
        txtEmailAddProfile = findViewById(R.id.txtEmailAddProfile);
        editEmailAddProfile = findViewById(R.id.editEmailAddProfile);
        editPasswordEmailProfile = findViewById(R.id.editPasswordEmailProfile);
        btnSaveEmailProfile = findViewById(R.id.btnSaveEmailProfile);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id","N/A");
        String oldEmail = sharedPreferences.getString("email","N/A");

        txtEmailAddProfile.setText(oldEmail);
        txtUserIdEmailProfile.setText(userId);

//        String newEmail = editEmailAddProfile.getText().toString().trim();

        btnSaveEmailProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String USERID = txtUserIdEmailProfile.getText().toString();
                String EMAIL = editEmailAddProfile.getText().toString();
                String PASSWORD = editPasswordEmailProfile.getText().toString();

                saveNewEmailAddProfile(USERID,EMAIL,PASSWORD);
            }
        });

    }

    // SAVE NEW EMAIL IN DATABASE
    public void  saveNewEmailAddProfile(String userId,String newEmail,String password){
        if (newEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(EditEmailAddActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "http://192.168.0.106:1505/project_1/updateEmail.php";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id",userId)
                .add("email",newEmail)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditEmailAddActivity.this, "Unable to change Email Address", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    runOnUiThread(() -> Toast.makeText(EditEmailAddActivity.this, "No response from server", Toast.LENGTH_SHORT).show());
                    return;
                }
                String responseData = response.body().string();

                runOnUiThread(() ->{
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String status = jsonObject.getString("status");

                        if(status.equals("success")){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email",newEmail);
                            editor.apply();

                            Toast.makeText(EditEmailAddActivity.this,"Email Add Updated Successfully",Toast.LENGTH_SHORT).show();

                            finish();
                        }else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(EditEmailAddActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
                        Toast.makeText(EditEmailAddActivity.this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}