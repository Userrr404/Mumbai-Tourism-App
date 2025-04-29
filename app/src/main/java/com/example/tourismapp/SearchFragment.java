package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
//    String url = "http://192.168.0.104/tourism/db_display_places.php";
    List<Model> imagelist;

    Model model;

    LinearLayoutManager linearLayoutManager;

    // SEARCH VIEW
    private SearchView searchViewSearch;

    LinearLayout horizontalScrollContainer;

//    SEARCH NAVIGATION BUTTON
    Button btnFunSearch,btnTempleSearch, btnFamilySearch,btnCoupleSearch,btnBeachSearch,btnMonumentSearch,btnCavesSearch,btnFortsSearch,btnMuseumsSearch,btnFreeEntrySearch;

    Button btnRefreshSearch;

    TextView txtInternetOffSearch;
    Adapter adapter;
    Set<String> savedPlacesIds = new HashSet<>();
    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewHome);

        // USE getContext() OR requireContext() instead of 'this' BECAUSE WE ARE IN FRAGMENT
        // linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        imagelist = new ArrayList<>();
        adapter = new Adapter(getContext(),imagelist,savedPlacesIds);
        recyclerView.setAdapter(adapter);

        horizontalScrollContainer = rootView.findViewById(R.id.horizontalScrollContainer);

        // Internet off
        btnRefreshSearch = rootView.findViewById(R.id.btnRefreshSearch);
        txtInternetOffSearch = rootView.findViewById(R.id.txtInternetOffSearch);

        btnRefreshSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUIBaseOnInternet();
            }
        });


//        SEARCH NAVIGATION BUTTON
        btnFunSearch = rootView.findViewById(R.id.btnFunSearch);
        btnTempleSearch = rootView.findViewById(R.id.btnTempleSearch);
        btnFamilySearch = rootView.findViewById(R.id.btnFamilySearch);
        btnCoupleSearch = rootView.findViewById(R.id.btnCoupleSearch);
        btnBeachSearch = rootView.findViewById(R.id.btnBeachSearch);
        btnMonumentSearch = rootView.findViewById(R.id.btnMonumentSearch);
        btnCavesSearch = rootView.findViewById(R.id.btnCavesSearch);
        btnFortsSearch = rootView.findViewById(R.id.btnFortSearch);
        btnMuseumsSearch = rootView.findViewById(R.id.btnMuseumsSearch);
        btnFreeEntrySearch = rootView.findViewById(R.id.btnFreeSearch);

        // SEARCH VIEW
        searchViewSearch = rootView.findViewById(R.id.searchViewSearch);
        searchViewSearch.clearFocus();
        searchViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        // SEARCH NAVIGATION BUTTON
        btnFunSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String funSearch = "fun";
                filteredList(funSearch);
            }
        });
//
        btnTempleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templeSearch = "temple";
                filteredList(templeSearch);
            }
        });
//
        btnFamilySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String familySearch = "family";
                filteredList(familySearch);
            }
        });

        btnCoupleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupleSearch = "couple";
                filteredList(coupleSearch);
            }
        });

        btnBeachSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beachSearch = "beach";
                filteredList(beachSearch);
            }
        });

        btnMonumentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monumentSearch = "monument";
                filteredList(monumentSearch);
            }
        });

        btnCavesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cavesSearch = "caves";
                filteredList(cavesSearch);
            }
        });

        btnFortsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fortsSearch = "forts";
                filteredList(fortsSearch);
            }
        });

        btnMuseumsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String museumsSearch = "museums";
                filteredList(museumsSearch);
            }
        });

        btnFreeEntrySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String freeSearch = "free";
                filteredList(freeSearch);
            }
        });

        return rootView;
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUIBaseOnInternet(); // Automatically update UI
        }
    };

    // Immediately run when fragment is opened
    // Checks current internet and updates UI
    @Override
    public void onResume(){
        super.onResume();
        requireActivity().registerReceiver(networkReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        updateUIBaseOnInternet();
    }

    // When internet state changes
    // Keeps UI in sync with connectivity changes
    @Override
    public void onPause(){
        super.onPause();
        requireActivity().unregisterReceiver(networkReceiver);
    }

    private void updateUIBaseOnInternet() {
        boolean isConnected = isInternetConnected();
        if(isConnected){
            txtInternetOffSearch.setVisibility(View.GONE);
            btnRefreshSearch.setVisibility(View.GONE);
            searchViewSearch.setVisibility(View.VISIBLE);
            horizontalScrollContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            txtInternetOffSearch.setVisibility(View.VISIBLE);
            btnRefreshSearch.setVisibility(View.VISIBLE);
            searchViewSearch.setVisibility(View.GONE);
            horizontalScrollContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }


    /**
     * Function to check if response is valid JSON
     */


    /* Once data is fetched, it's shown in the RecyclerView, and even if you later turn off the internet, it continues to display the fetched data.
       If internet is off (even after previous fetch), hide the data and show a message like “Internet is off” and a “Refresh” button.
       write in onResume()
       This ensures that when the fragment becomes visible again, it checks the internet.
    */


    // SEARCH VIEW
    private void filteredList(String text) {

        if(text.trim().isEmpty()){
            imagelist.clear();
            adapter.setFilteredList(imagelist);  // Clear the list if no search text
            return;
        }

        String searchURL = "http://10.0.2.2/tourism/DB_search_user_places.php";

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

                            String urlImage = "http://10.0.2.2/tourism/"+url2;

                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact);
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
}