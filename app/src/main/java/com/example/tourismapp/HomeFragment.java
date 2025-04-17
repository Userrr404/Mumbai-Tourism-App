package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    //    String url = "http://192.168.0.100:1505/project_1/getimage.php";
//    String url = "http://192.168.0.100:1505/project_1/db_getimg.php";
    String url = "http://192.168.0.108/tourism/db_display_places.php";
    List<Model> imagelist;

    Model model;

    LinearLayoutManager linearLayoutManager;

    Adapter adapter;

    Button btnRefreshHome;

    TextView txtInternetOffHome;

    View fakeScreen;

    // Create a BroadcastReceiver to Detect Internet Connectivity Changes
    private BroadcastReceiver networkReceiver;

    // Why if  my mysql database is off then why load twice FakeHomeActivity.java. but i want to only dhow 1 time FakeHomeActivity.java instead of twice.
    // Solution use a flag to ensure FakeHomeActivity is only started once
    private boolean fakeHomeActivityStarted = false;


    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewHome);

        // USE getContext() OR requireContext() instead of 'this' BECAUSE WE ARE IN FRAGMENT
        // linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        imagelist = new ArrayList<>();
        adapter = new Adapter(getContext(),imagelist);
        recyclerView.setAdapter(adapter);

        btnRefreshHome = rootView.findViewById(R.id.btnRefreshHome);
        txtInternetOffHome = rootView.findViewById(R.id.txtInternetOffHome);
        fakeScreen = rootView.findViewById(R.id.fakeHomeScreenLayoutHome);

        checkInternetAndLoadData();

        btnRefreshHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the loading screen again
//                fakeScreen.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//                txtInternetOffHome.setVisibility(View.GONE);
//                btnRefreshHome.setVisibility(View.GONE);
//
//                // Wait 3 sec and try loading data again
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        checkInternetAndLoadData();
//                        fakeScreen.setVisibility(View.GONE);
//                    }
//                },3000);
                checkInternetAndLoadData();
            }
        });

        fakeScreen.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fakeScreen.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//        },4000);

//        getImage();

        return rootView;
    }

    private void checkInternetAndLoadData(){
        if(isInternetAvailable()){

            // show loading only if data isn't already fetched
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
//                fakeScreen.setVisibility(View.GONE);
                imagelist.clear();

                if(!isValidJson(response)){
                    // If response is invalid, redirect to FakeHomeScreen Activity
//                    Toast.makeText(getContext(), "Database is offline.", Toast.LENGTH_SHORT).show();
                    // Give fakeHomeActivityStarted true to store only one time call fakeHomeActivity
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

                            // String id = object.getString("id");
                            // String url2 = object.getString("image");
                        // String name = object.getString("name");
                        // String info = object.getString("info");
                        //
                        // String urlImage = "http://192.168.0.100:1505/project_1"+url2;
                        // model = new Model(id, urlImage, name, info);

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
                            // http://localhost:1505/project_1/tourist/gateway_of_india.jpg

                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact);
                            imagelist.add(model);
//                            adapter.notifyDataSetChanged();
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

    private boolean isInternetAvailable(){
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

//        if(!isInternetAvailable()){
//            // If the internet is lost after data is loaded, clear the RecyclerView
//            imagelist.clear();
//            adapter.notifyDataSetChanged();
//
//            // Show "Internet is off" message and Refresh button
//            txtInternetOffHome.setVisibility(View.VISIBLE);
//            btnRefreshHome.setVisibility(View.VISIBLE);
//
//            // Hide loading screen and RecyclerView
//            fakeScreen.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
//        }else{
//            // If internet is available, proceed with fetching data
//            checkInternetAndLoadData();
//        }
//        checkInternetAndLoadData();
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

    // Update UI When Internet is Turned Off Without Changing Fragments
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