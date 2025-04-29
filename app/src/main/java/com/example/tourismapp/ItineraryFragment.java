package com.example.tourismapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourismapp.R;
import com.example.tourismapp.ItineraryAdapter;
import com.example.tourismapp.Itinerary;
import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ItineraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItineraryAdapter adapter;
    private ArrayList<Itinerary> itineraryList;
    private String userId;  // Pass the logged-in user id

    public ItineraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itinerary, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewItinerary);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itineraryList = new ArrayList<>();
        adapter = new ItineraryAdapter(getContext(), itineraryList);
        recyclerView.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = preferences.getString("user_id","N/A");


        loadItineraryData();

        return view;
    }

    private void loadItineraryData() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ApiClient.GET_ITINERARY_URL + "?user_id=" + userId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, java.io.IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                String jsonResponse = response.body().string();

                try {
                    JSONArray jsonArray = new JSONArray(jsonResponse);

                    itineraryList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        Itinerary itinerary = new Itinerary(
                                obj.getString("name"),
                                obj.getString("image_path"),
                                obj.getString("booking_date"),
                                obj.getInt("number_of_people")
                        );
                        itineraryList.add(itinerary);
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
