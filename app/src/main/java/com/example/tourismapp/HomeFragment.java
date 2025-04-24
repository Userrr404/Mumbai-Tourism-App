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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    String url = "http://192.168.0.100/tourism/DB_display_tourist_places.php";
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
    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
        return rootView;
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(String userId, String username) {
        String savedPlacesURL = "http://192.168.0.100/tourism/TB_get_user_saved_places.php?user_id=" + userId + "&username=" + username;

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
                            String urlImage = "http://192.168.0.100/tourism/"+url2;
                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact);
                            imagelist.add(model);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                fakeScreen.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        },5000);
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