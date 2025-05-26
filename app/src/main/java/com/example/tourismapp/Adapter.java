package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tourismapp.Utills.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Adapter extends RecyclerView.Adapter<ImageViewHolder>{
    private Context context;
    private List<Model> imagelist;

    // For get user details
    private String userId;
    private String username;
    private Set<String> savedPlaceIds;
    public Adapter(Context context, List<Model> imagelist,Set<String> savedPlaceIds){
        this.context = context;
        this.imagelist = imagelist;
        this.savedPlaceIds = savedPlaceIds;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.userId = sharedPreferences.getString("user_id", "N/A");
        this.username = sharedPreferences.getString("username","N/A");
        if ("N/A".equals(this.username)) {
            Toast.makeText(context, "Error: username not found in shared preferences", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<Model> filteredList){
        this.imagelist = filteredList;
        notifyDataSetChanged();
    }
    public void setSavedPlaceIds(Set<String> savedIds){
        this.savedPlaceIds = savedIds;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview,parent,false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Model model = imagelist.get(position);
        Glide.with(context).load(model.getImage()).into(holder.item_imageview);
        holder.item_txtImageName.setText(model.getName());
        String placeId = model.getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("place_id",model.getId());
                intent.putExtra("urlImage",model.getImage());
                intent.putExtra("name",model.getName());
                intent.putExtra("description",model.getDescription());
                intent.putExtra("category",model.getCategory());
                intent.putExtra("tags",model.getTags());
                intent.putExtra("exact_location",model.getExact_location());
                intent.putExtra("timing",model.getTiming());
                intent.putExtra("fees",model.getFees());
                intent.putExtra("contact",model.getContact());
                intent.putExtra("latitude",model.getLatitude());
                intent.putExtra("longitude",model.getLongitude());
                context.startActivity(intent);
            }
        });
        if(savedPlaceIds.contains(model.getId())){
            holder.normalSaveIcon.setVisibility(View.GONE);
            holder.filledSaveIcon.setVisibility(View.VISIBLE);
        }else{
            holder.normalSaveIcon.setVisibility(View.VISIBLE);
            holder.filledSaveIcon.setVisibility(View.GONE);
        }
        holder.normalSaveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlace(model,holder.normalSaveIcon);

                // optional to store state of save place
            }
        });
        holder.filledSaveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlace(model,holder.filledSaveIcon,holder.normalSaveIcon);
            }
        });
    }
    private void removePlace(Model model, ImageView filledSaveIcon, ImageView normalSaveIcon) {
        String removePlaceURL = ApiClient.REMOVE_PLACE_URL;
        StringRequest removeRequest = new StringRequest(Request.Method.POST, removePlaceURL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int success = jsonResponse.getInt("success");
                        String message = jsonResponse.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        if (success == 1) {
                            filledSaveIcon.setVisibility(View.GONE);
                            normalSaveIcon.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("username", username);
                params.put("place_id", model.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(removeRequest);
    }
    private void savePlace(Model model, ImageView itemNormalSaveIcon) {
        String savePlaceURL = ApiClient.SAVE_PLACE_URL;
        if (username == null || username.equals("N/A") || username.isEmpty()) {
            Toast.makeText(context, "Username missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest savePlaceRequest = new StringRequest(Request.Method.POST, savePlaceURL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        int success = jsonResponse.getInt("success");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (success == 1) {
//                            itemNormalSaveIcon.setImageResource(R.drawable.filed_bookmark_svgrepo_com); // Switch icon
                            itemNormalSaveIcon.setVisibility(View.GONE);
                            ((View)((View)itemNormalSaveIcon.getParent())).findViewById(R.id.item_Filed_SaveIcon).setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("username", username);
                params.put("place_id", model.getId()); // Assuming your model has getId()
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(savePlaceRequest);
    }
    @Override
    public int getItemCount() {
        return imagelist.size();
    }
}

class ImageViewHolder extends RecyclerView.ViewHolder{
    TextView item_txtImageName;
    ImageView item_imageview;
    ImageView normalSaveIcon, filledSaveIcon;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        item_imageview = itemView.findViewById(R.id.item_imageView);
        item_txtImageName = itemView.findViewById(R.id.item_txtImageName);
        normalSaveIcon = itemView.findViewById(R.id.item_Normal_saveIcon);
        filledSaveIcon = itemView.findViewById(R.id.item_Filed_SaveIcon);
    }
}
