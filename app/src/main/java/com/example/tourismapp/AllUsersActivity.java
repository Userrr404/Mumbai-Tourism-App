package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> userList;

    TextView textLoggedInUserAdmin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);  // Your layout for AllUsersActivity

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textLoggedInUserAdmin = findViewById(R.id.textLoggedInUserAdmin);


        // Initialize the user list and fetch data
        userList = new ArrayList<>();
        fetchUsers();

        usersAdapter = new UsersAdapter(this,userList);
        recyclerView.setAdapter(usersAdapter);
    }

    private void fetchUsers() {
        // Fetch user data from your server via API (using OkHttp or any other networking library)
        // This is just a placeholder for the actual networking code

        String url = "http://192.168.0.101/tourism/admin_api/db_get_all_users.php";  // Replace with actual API endpoint
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();  // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject userObj = jsonArray.getJSONObject(i);
                            String username = userObj.getString("username");
                            String email = userObj.getString("email");
                            String password = userObj.getString("password");
                            String full_name = userObj.getString("full_name");
                            // Add users to the list
                            userList.add(new User(username, email,password,full_name));
                        }

                        // Update UI on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                usersAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
