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
import java.util.List;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    String url = "http://192.168.0.108/tourism/db_display_places.php";
    List<Model> imagelist;

    Model model;

    LinearLayoutManager linearLayoutManager;

    // SEARCH VIEW
    private SearchView searchViewSearch;

//    SEARCH NAVIGATION BUTTON
    Button btnFunSearch,btnTempleSearch, btnFamilySearch,btnCoupleSearch,btnBeachSearch,btnMonumentSearch,btnCavesSearch,btnFortsSearch,btnMuseumsSearch,btnFreeEntrySearch;

    Adapter adapter;

    Button btnRefreshSearch;

    TextView txtInternetOffSearch;

    View fakeScreen;

    // Create a BroadcastReceiver to Detect Internet Connectivity Changes
    private BroadcastReceiver networkReceiver;

    private boolean fakeHomeActivityStarted = false;
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
        adapter = new Adapter(getContext(),imagelist);
        recyclerView.setAdapter(adapter);


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

        // FAKE
        btnRefreshSearch = rootView.findViewById(R.id.btnRefreshSearch);
        txtInternetOffSearch = rootView.findViewById(R.id.txtInternetOffSearch);
        fakeScreen = rootView.findViewById(R.id.fakeHomeScreenLayoutSearch);

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

        checkInternetAndLoadData();
        return rootView;
    }

    private void checkInternetAndLoadData() {
        if(isInternetAvailable()){

            // show loading only if data isn't already fetched
            if(imagelist.isEmpty()){
                fakeScreen.setVisibility(View.VISIBLE);
            }

            recyclerView.setVisibility(View.VISIBLE);
            txtInternetOffSearch.setVisibility(View.GONE);
            btnRefreshSearch.setVisibility(View.GONE);
            searchViewSearch.setVisibility(View.VISIBLE);
            getImage();
        }else{
            recyclerView.setVisibility(View.GONE);
            txtInternetOffSearch.setVisibility(View.VISIBLE);
            btnRefreshSearch.setVisibility(View.VISIBLE);
            fakeScreen.setVisibility(View.GONE);
            searchViewSearch.setVisibility(View.GONE);
        }
    }


    public void getImage(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
//                fakeScreen.setVisibility(View.GONE);
                imagelist.clear();

                if(!isValidJson(response)){
                    // If response is invalid, redirect to FakeHomeScreen Activity
//                    Toast.makeText(getContext(), "Database is offline.", Toast.LENGTH_SHORT).show();
                    // Give fakeHomeActivityStarted true to store only one time call fakeHomeActivity

                    // IS BETTER TO USE requireActivity() or isAdded() to ensure fragment is attached to its activity
                    // TO PREVENT FOLLOWING ERROR WE USE
//                    java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String android.content.Context.getPackageName()' on a null object reference
//                    at android.content.ComponentName.<init>(ComponentName.java:133)
//                    at android.content.Intent.<init>(Intent.java:7994)
//                    at com.example.tourismapp.HomeFragment$2.onResponse(HomeFragment.java:165)
                    if(!fakeHomeActivityStarted && isAdded()){
                        fakeHomeActivityStarted = true;
//                        Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                        Intent iFake = new Intent(requireActivity(), FakeHomeActivity.class);
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

                            String id = object.getString("id");
                            String url2 = object.getString("image_path");
                            String name = object.getString("name");
                            String description = object.getString("description");
                            String category = object.getString("category");
                            String tags = object.getString("tags");
                            String exact_location = object.getString("exact_location");
                            String timing = object.getString("timing");
                            String fees = object.getString("fees");
                            String contact = object.getString("contact");

                            String urlImage = "http://192.168.0.108/tourism/"+url2;

                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact);
                            imagelist.add(model);
                        }

                        // Delay Showing the data by 3 seconds
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
                    // Give fakeHomeActivityStarted true to store only one time call fakeHomeActivity
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
//                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                // if database doesn't content data
                Toast.makeText(getContext(),"Error fetching data results",Toast.LENGTH_SHORT).show();
//                System.out.println("Volley Error: " + error.getMessage());
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                fakeScreen.setVisibility(View.GONE);

                // IS BETTER TO USE requireActivity() or isAdded() to ensure fragment is attached to its activity
                if(!fakeHomeActivityStarted && isAdded()){
                    fakeHomeActivityStarted = true;
//                    Intent iFake = new Intent(getContext(), FakeHomeActivity.class);
                    Intent iFake = new Intent(requireActivity(), FakeHomeActivity.class);
                    startActivity(iFake);
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
    }

    /**
     * Function to check if response is valid JSON
     */

    private boolean isValidJson(String response){
        try {
            new JSONObject(response);
            return true;
        }catch (JSONException e){
            return false;
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /* Once data is fetched, it's shown in the RecyclerView, and even if you later turn off the internet, it continues to display the fetched data.
       If internet is off (even after previous fetch), hide the data and show a message like “Internet is off” and a “Refresh” button.
       write in onResume()
       This ensures that when the fragment becomes visible again, it checks the internet.
    */

    @Override
    public void onResume(){

        super.onResume();
//        This ensures that if the user goes back to HomeFragment, the logic can work again.
        fakeHomeActivityStarted = false;
    }

    //Register the Receiver in onStart() and Unregister in onStop()

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
                    txtInternetOffSearch.setVisibility(View.VISIBLE);
                    btnRefreshSearch.setVisibility(View.VISIBLE);
                    fakeScreen.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    searchViewSearch.setVisibility(View.GONE);
                }else{
                    checkInternetAndLoadData();
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkReceiver,filter);
    }

    // SEARCH VIEW
    private void filteredList(String text) {
        List<Model> filteredList = new ArrayList<>();
        for(Model model1 : imagelist){
            if(model1.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model1);
            } else if (model1.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model1);
            } else if (model1.getTags().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model1);
            } else if (model1.getFees().equalsIgnoreCase(text)) { // use equals() instead of contains()
                filteredList.add(model1);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(requireActivity(),"No data found",Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }
}