package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    //    String url = "http://192.168.0.100:1505/project_1/getimage.php";
    String url = "http://192.168.0.106:1505/project_1/db_getimg.php";
    List<Model> imagelist;

    Model model;

    LinearLayoutManager linearLayoutManager;

    // SEARCH VIEW
    private SearchView searchViewSearch;

//    SEARCH NAVIGATION BUTTON
    Button btnFunSearch,btnTempleSearch, btnFamilySearch,btnCoupleSearch,btnBeachSearch,btnMonumentSearch,btnCavesSearch,btnFortsSearch,btnMuseumsSearch,btnFreeEntrySearch;

    Adapter adapter;
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
        btnFortsSearch = rootView.findViewById(R.id.btnFortsSearch);
        btnMuseumsSearch = rootView.findViewById(R.id.btnMuseumsSearch);
        btnFreeEntrySearch = rootView.findViewById(R.id.btnFreeEntrySearch);

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

        btnTempleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templeSearch = "temple";
                filteredList(templeSearch);
            }
        });

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
                String coupleSearch = "romantic";
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

        getImage();
        return rootView;
    }

    // SEARCH VIEW
    private void filteredList(String text) {
        List<Model> filteredList = new ArrayList<>();
        for(Model model1 : imagelist){
            if(model1.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(model1);
            } else if (model1.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model1);
            }else if(model1.getTags().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(model1);
            } else if (model1.getFees().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(model1);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(requireActivity(),"No data found",Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    public void getImage(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                imagelist.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

//                            String id = object.getString("id");
//                            String url2 = object.getString("image");
//                            String name = object.getString("name");
//                            String info = object.getString("info");
//
//                            String urlImage = "http://192.168.0.100:1505/project_1"+url2;
//
//                            model = new Model(id, urlImage, name, info);

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

                            String urlImage = "http://192.168.0.106:1505/project_1/"+url2;
//                            http://localhost:1505/project_1/tourist/gateway_of_india.jpg

                            model = new Model(id,urlImage,name,description,category,tags,exact_location,timing,fees,contact);
                            imagelist.add(model);
//                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
//                System.out.println("Volley Error: " + error.getMessage());
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(request);
    }
}