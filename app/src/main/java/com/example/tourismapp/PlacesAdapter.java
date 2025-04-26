package com.example.tourismapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PlacesAdapter extends RecyclerView.Adapter<PlaceViewHolder> {
    private Context context;

    private List<Place> placeList;

    public PlacesAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.nameTextView.setText(place.getName());
//        holder.descriptionTextView.setText(place.getDescription());
        // Load image using Glide or Picasso
        Glide.with(holder.itemView.getContext()).load(place.getImagePath()).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPlaceActivity.class);
            intent.putExtra("place_id", place.getPlaceId());
            intent.putExtra("image_url", place.getImagePath());
            intent.putExtra("place_name", place.getName());
            intent.putExtra("description", place.getDescription());
            intent.putExtra("category", place.getCategory());
            intent.putExtra("tags", place.getTags());
            intent.putExtra("exact_location", place.getExactLocation());
            intent.putExtra("timing", place.getTiming());
            intent.putExtra("fees", place.getFees());
            intent.putExtra("contact", place.getContact());
            intent.putExtra("location", place.getLocation());
            context.startActivity(intent);
        });

        holder.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPlaceActivity.class);
                intent.putExtra("place_id", place.getPlaceId()); // important
                intent.putExtra("place_name", place.getName());
                intent.putExtra("image_url", place.getImagePath());
                intent.putExtra("description", place.getDescription());
                intent.putExtra("category", place.getCategory());
                intent.putExtra("tags", place.getTags());
                intent.putExtra("exact_location", place.getExactLocation());
                intent.putExtra("timing", place.getTiming());
                intent.putExtra("fees", place.getFees());
                intent.putExtra("contact", place.getContact());
                intent.putExtra("location", place.getLocation());
                context.startActivity(intent);
            }
        });
        
        holder.textRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeId = place.getPlaceId();
                removePlaceFromDatabase(placeId);
            }
        });

    }

    private void removePlaceFromDatabase(String placeId) {

        String url = "http://192.168.0.101/tourism/admin_api/db_remove_place.php?place_id=" + placeId; // Replace with actual URL

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Failed to remove place", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    ((Activity) context).runOnUiThread(() -> {
                        placeList.removeIf(place -> place.getPlaceId().equals(placeId));
                        notifyDataSetChanged();
                        Toast.makeText(context, "Place removed successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Error removing place", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

}

class PlaceViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView;
    ImageView imageView;
    TextView textEdit, textRemove;


    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.textPlaceName);
        imageView = itemView.findViewById(R.id.imagePlace);
        textEdit = itemView.findViewById(R.id.textEdit);
        textRemove = itemView.findViewById(R.id.textRemove);
    }
}

