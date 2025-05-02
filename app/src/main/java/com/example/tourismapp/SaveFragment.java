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
import android.util.Log;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SaveFragment extends Fragment {

    RecyclerView recyclerView;
    String URL = "http://10.0.2.2/tourism/TB_display_user_places.php";
    List<SavePlaceModel> savePlaceModelList;
    SavePlaceModel savePlaceModel;
    LinearLayoutManager linearLayoutManager;
    SavePlaceAdapter savePlaceAdapter;

    String userId, username;

    TextView txtEmptyMessageSave;

    Button btnRefreshSave;
    TextView txtInternetOffSave;

    private BroadcastReceiver networkReceiver;
    private boolean fakeHomeActivityStarted = false;


    public SaveFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_save, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewSave);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        savePlaceModelList = new ArrayList<>();
        savePlaceAdapter = new SavePlaceAdapter(getContext(),savePlaceModelList);
        recyclerView.setAdapter(savePlaceAdapter);

        btnRefreshSave = rootView.findViewById(R.id.btnRefreshSave);
        txtInternetOffSave = rootView.findViewById(R.id.txtInternetOffSave);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "N/A");
        username = sharedPreferences.getString("username", "N/A");

        return rootView;
    }

    private void checkInternetAndLoadData(){
        if(isInternetAvailable()){
//            if(savePlaceModelList.isEmpty()){
//                fakeScreen.setVisibility(View.VISIBLE);
//            }

            recyclerView.setVisibility(View.VISIBLE);
            txtInternetOffSave.setVisibility(View.GONE);
            btnRefreshSave.setVisibility(View.GONE);
            getUserSavedPlaces();
        }else{
            recyclerView.setVisibility(View.GONE);
            txtInternetOffSave.setVisibility(View.VISIBLE);
            btnRefreshSave.setVisibility(View.VISIBLE);
//            fakeScreen.setVisibility(View.GONE);
        }
    }

    private void getUserSavedPlaces() {

        String finalUrl = URL + "?user_id=" + userId + "&username=" + username;

        StringRequest request = new StringRequest(Request.Method.POST, finalUrl, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                savePlaceModelList.clear();

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
//                            String urlImage = "http://192.168.0.100/tourism/"+url2;
                            savePlaceModel = new SavePlaceModel(id,url2,name,description,category,tags,exact_location,timing,fees,contact,latitude,longitude);
                            savePlaceModelList.add(savePlaceModel);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                savePlaceAdapter.notifyDataSetChanged();
//                                fakeScreen.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        },5000);
                    }else{
                        Toast.makeText(getContext(), "No saved places found.", Toast.LENGTH_SHORT).show();
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

                Context context = requireContext().getApplicationContext();  // Get the context (activity) associated with the fragment
                if (context != null) {
                    Toast.makeText(getContext(),"Error fetching data results",Toast.LENGTH_SHORT).show();

                    if(!fakeHomeActivityStarted){
                        fakeHomeActivityStarted = true;
                        Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                        startActivity(iFake);
                    }

                } else {
                    Log.e("SaveFragment", "Context is null, can't show Toast.");
                }
//                fakeScreen.setVisibility(View.GONE);
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
                    savePlaceModelList.clear();
                    savePlaceAdapter.notifyDataSetChanged();
                    txtInternetOffSave.setVisibility(View.VISIBLE);
                    btnRefreshSave.setVisibility(View.VISIBLE);
//                    fakeScreen.setVisibility(View.GONE);
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