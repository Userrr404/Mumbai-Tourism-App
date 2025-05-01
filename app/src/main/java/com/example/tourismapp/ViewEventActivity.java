package com.example.tourismapp;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity {

    RecyclerView recyclerEvents;
    EventAdapter eventAdapter;
    ArrayList<EventModel> eventList;


    String fetchUrl = "http://10.0.2.2/tourism/db_fetch_events.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_view_event);

        recyclerEvents = findViewById(R.id.recyclerEvents);

        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();

        eventAdapter = new EventAdapter(this, eventList);
        recyclerEvents.setAdapter(eventAdapter);

        loadEvents();

    }

    private void loadEvents() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, fetchUrl, null,
                response -> {
                    try {
                        eventList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String id = obj.getString("id");
                            String image = obj.getString("image");
                            String name = obj.getString("name");
                            String date = obj.getString("date");
                            String description = obj.getString("description");

                            eventList.add(new EventModel(id, image, name, date, description));
                        }
                        eventAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(this, "Parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
