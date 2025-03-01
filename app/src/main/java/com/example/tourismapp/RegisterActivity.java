package com.example.tourismapp;

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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {

    // My insert data API
    private static final String BASE_URL = "http://192.168.0.100:1505/project_1/db_insert.php";

    // OkHttp library
    private final OkHttpClient client = new OkHttpClient();

    EditText editUsernameReg, editEmailAddReg, editPasswordReg, editConfirmPassReg;
    Button btnSignupReg;
    TextView txtRedirectLoginReg;

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
        setContentView(R.layout.activity_register);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        editUsernameReg = findViewById(R.id.editUsernameReg);
        editEmailAddReg = findViewById(R.id.editEmailAddReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        editConfirmPassReg = findViewById(R.id.editConfirmPassReg);
        btnSignupReg = findViewById(R.id.btnRegisterReg);
        txtRedirectLoginReg = findViewById(R.id.clickableRedirectLoginReg);

        // Redirect login activity
        txtRedirectLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(iLogin);
            }
        });

        // Insert data in database
        btnSignupReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsernameReg.getText().toString();
                String email = editEmailAddReg.getText().toString();
                String password = editPasswordReg.getText().toString();
                String confirmPaas = editConfirmPassReg.getText().toString();

                if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPaas.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All fields are Required", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(confirmPaas)){
                        savaData(username,email,password);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void savaData(String username, String email, String password){
        // CREATE REQUEST BODY
        RequestBody formData = new FormBody.Builder()
                .add("username",username)
                .add("email",email)
                .add("password",password)
                .build();

        // CREATE REQUEST
        Request request = new Request.Builder().url(BASE_URL).post(formData).build();

        // CALL THE REQUEST
        Call call = client.newCall(request);

        // MANAGE THE CALL
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                editUsernameReg.setText("");
                editEmailAddReg.setText("");
                editPasswordReg.setText("");
                editConfirmPassReg.setText("");
                txtRedirectLoginReg.setText(e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String resp = response.body().string();

                if(response.isSuccessful()){
                    // RUN VIEW RELATED CODE ON MAIN THREAD
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Toast.makeText(RegisterActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                                editUsernameReg.setText("");
                                editEmailAddReg.setText("");
                                editPasswordReg.setText("");
                                editConfirmPassReg.setText("");
                                Intent iLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(iLogin);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    // END OF RUNNING ON MAIN THREAD
                }
            }
        });
    }
}