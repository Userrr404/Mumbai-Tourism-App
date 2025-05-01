package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExploreFragment extends Fragment {
    private SearchView searchView;
    RecyclerView recyclerView;
    String url = ApiClient.DISPLAY_PLACES_URL;
    List<Model> imagelist;
    Model model;
    LinearLayoutManager linearLayoutManager;
    Adapter adapter;
    Button btnRefreshHome;
    TextView txtInternetOffHome;
    View fakeScreen;
    private BroadcastReceiver networkReceiver;
    private boolean fakeHomeActivityStarted = false;
    Set<String> savedPlacesIds = new HashSet<>();
    public ExploreFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewHome);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        imagelist = new ArrayList<>();
        adapter = new Adapter(getContext(),imagelist,savedPlacesIds);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "N/A");
        String username = sharedPreferences.getString("username", "N/A");

        JsonObjectRequest request = getJsonObjectRequest(userId, username);

        Volley.newRequestQueue(requireContext()).add(request);

        btnRefreshHome = rootView.findViewById(R.id.btnRefreshHome);
        txtInternetOffHome = rootView.findViewById(R.id.txtInternetOffHome);
        fakeScreen = rootView.findViewById(R.id.fakeHomeScreenLayoutHome);
        checkInternetAndLoadData();
        btnRefreshHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndLoadData();
            }
        });
        fakeScreen.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        // SEARCH VIEW
        searchView = rootView.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setIconifiedByDefault(false);  // Ensures SearchView is not iconified by default.
        searchView.setQueryHint("Search Places"); // Set the hint text.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList(newText);
                return true;
            }
        });

        return rootView;
    }

    private void filteredList(String text) {

        if(text.trim().isEmpty()){
            imagelist.clear();
//            adapter.setFilteredList(imagelist);  // Clear the list if no search text
            getImage();
            return;
        }

        String searchURL = ApiClient.SEARCH_PLACES_URL;

        StringRequest request = new StringRequest(Request.Method.POST, searchURL, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
//                fakeScreen.setVisibility(View.GONE);
                imagelist.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("place_id");
                            String url2 = object.getString("image_path");
                            String name = object.getString("name");
                            String description = object.getString("description");
                            String category = object.getString("category");
                            String tags = object.getString("tags");
                            String exact_location = object.getString("exact_location");
                            String timing = object.getString("timing");
                            String fees = object.getString("fees");
                            String contact = object.getString("contact");
                            String latitude = object.getString("latitude");
                            String longitude = object.getString("longitude");

                            String urlImage = ApiClient.SHORT_URL+url2;

                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact,latitude,longitude);
                            imagelist.add(model);
                        }

                        adapter.setFilteredList(imagelist);
                    }else{
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                        imagelist.clear();
                        adapter.setFilteredList(imagelist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Error Parsing data", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"Error fetching data results",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String ,String> params = new HashMap<>();
                params.put("query",text.trim()); // trim the spaces
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);

    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(String userId, String username) {
        String savedPlacesURL = ApiClient.GET_USER_SAVE_PLACES_URL + "user_id=" + userId + "&username=" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, savedPlacesURL, null,
                response -> {
                    try {
                        JSONArray savedArray = response.getJSONArray("saved_places");
                        Set<String> savedPlaceIds = new HashSet<>();
                        for (int i = 0; i < savedArray.length(); i++) {
                            savedPlaceIds.add(savedArray.getString(i));
                        }

                        // Now pass savedPlaceIds to your Adapter
                        adapter = new Adapter(getContext(), imagelist, savedPlaceIds);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });
        return request;
    }

    private void checkInternetAndLoadData(){
        if(isInternetAvailable()){
            if(imagelist.isEmpty()){
                fakeScreen.setVisibility(View.VISIBLE);
            }

            recyclerView.setVisibility(View.VISIBLE);
            txtInternetOffHome.setVisibility(View.GONE);
            btnRefreshHome.setVisibility(View.GONE);
            getImage();
        }else{
            recyclerView.setVisibility(View.GONE);
            txtInternetOffHome.setVisibility(View.VISIBLE);
            btnRefreshHome.setVisibility(View.VISIBLE);
            fakeScreen.setVisibility(View.GONE);
        }
    }
        public void getImage(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                imagelist.clear();

                if(!isValidJson(response)){
                    if(!fakeHomeActivityStarted){
                        fakeHomeActivityStarted = true;
                        Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                        startActivity(iFake);
                    }
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("place_id");
                            String url2 = object.getString("image_path");
                            String name = object.getString("name");
                            String description = object.getString("description");
                            String category = object.getString("category");
                            String tags = object.getString("tags");
                            String exact_location = object.getString("exact_location");
                            String timing = object.getString("timing");
                            String fees = object.getString("fees");
                            String contact = object.getString("contact");
                            String latitude = object.getString("latitude");
                            String longitude = object.getString("longitude");
                            String urlImage = ApiClient.SHORT_URL + url2;
                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact,latitude,longitude);
                            imagelist.add(model);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                fakeScreen.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        },2000);
                    }
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Error Parsing data", Toast.LENGTH_SHORT).show();
                    if(!fakeHomeActivityStarted){
                        fakeHomeActivityStarted = true;
                        Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                        startActivity(iFake);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error fetching data results",Toast.LENGTH_SHORT).show();
                fakeScreen.setVisibility(View.GONE);

                if(!fakeHomeActivityStarted){
                    fakeHomeActivityStarted = true;
                    Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                    startActivity(iFake);
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
    }
    private boolean isValidJson(String response){
        try {
            new JSONObject(response);
            return true;
        }catch (JSONException e){
            return false;
        }
    }
    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    @Override
    public void onResume(){

        super.onResume();
        fakeHomeActivityStarted = false;
    }
    @Override
    public void onStart(){

        super.onStart();
        registerNetworkReceiver();
    }
    @Override
    public void onStop(){

        super.onStop();
        if(networkReceiver != null){
            requireActivity().unregisterReceiver(networkReceiver);
        }
    }
    private void registerNetworkReceiver(){
        networkReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                if(!isInternetAvailable()){
                    imagelist.clear();
                    adapter.notifyDataSetChanged();
                    txtInternetOffHome.setVisibility(View.VISIBLE);
                    btnRefreshHome.setVisibility(View.VISIBLE);
                    fakeScreen.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    checkInternetAndLoadData();
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkReceiver,filter);
    }
}