package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    RecyclerView recyclerViewEvents;
    EventAdapter eventAdapter;
    ArrayList<EventModel> eventList;
    TextView txtInternetOffEvents;
    Button btnRefreshEvents;

    String URL_EVENTS = "http://10.0.2.2/tourism/get_events.php";

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        txtInternetOffEvents = view.findViewById(R.id.txtInternetOffEvents);
        btnRefreshEvents = view.findViewById(R.id.btnRefreshEvents);

        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), eventList);
        recyclerViewEvents.setAdapter(eventAdapter);

        btnRefreshEvents.setOnClickListener(v -> loadEvents());

        loadEvents();

        return view;
    }

    private void loadEvents() {
        if (!isInternetAvailable()) {
            recyclerViewEvents.setVisibility(View.GONE);
            txtInternetOffEvents.setVisibility(View.VISIBLE);
            btnRefreshEvents.setVisibility(View.VISIBLE);
            return;
        }

        recyclerViewEvents.setVisibility(View.VISIBLE);
        txtInternetOffEvents.setVisibility(View.GONE);
        btnRefreshEvents.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_EVENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String image = object.getString("image");
                            String name = object.getString("name");
                            String date = object.getString("date");
                            String description = object.getString("description");

                            eventList.add(new EventModel(id, image, name, date, description));
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing event data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error loading events", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
