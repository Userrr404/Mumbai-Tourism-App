package com.example.tourismapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class SavePlaceAdapter extends RecyclerView.Adapter<SaveImageAdapter> {

    private Context context;
    private List<SavePlaceModel> savePlaceModelList;

    private String userId;
    private String username;

    public SavePlaceAdapter(Context context, List<SavePlaceModel> savePlace) {
        this.context = context;
        this.savePlaceModelList = savePlace;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.userId = sharedPreferences.getString("user_id", "N/A");
        this.username = sharedPreferences.getString("username", "N/A");

        if ("N/A".equals(this.username)) {
            Toast.makeText(context, "Error: username not found in shared preferences", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public SaveImageAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_save_place, parent, false);
        return new SaveImageAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveImageAdapter holder, int position) {
        SavePlaceModel savePlaceModel = savePlaceModelList.get(position);

        Glide.with(context).load(savePlaceModel.getImage()).into(holder.item_Img_user_Save);
        holder.item_txt_user_Save.setText(savePlaceModel.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("urlImage", savePlaceModel.getImage());
            intent.putExtra("name", savePlaceModel.getName());
            intent.putExtra("description", savePlaceModel.getDescription());
            intent.putExtra("category", savePlaceModel.getCategory());
            intent.putExtra("tags", savePlaceModel.getTags());
            intent.putExtra("exact_location", savePlaceModel.getExact_location());
            intent.putExtra("timing", savePlaceModel.getTiming());
            intent.putExtra("fees", savePlaceModel.getFees());
            intent.putExtra("contact", savePlaceModel.getContact());
            intent.putExtra("latitude", savePlaceModel.getLatitude());
            intent.putExtra("longitude", savePlaceModel.getLongitude());
            context.startActivity(intent);
        });

        // ❌ DELETE on Save Icon Click
        holder.item_saveIcon_Save.setOnClickListener(v -> {
            deletePlaceFromServer(savePlaceModel.getName(), position);
        });
    }

    private void deletePlaceFromServer(String placeName, int position) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("user_id", userId)
                .add("username", username)
                .add("name", placeName)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/tourism/db_remove_saved_place.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((android.app.Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Failed to connect server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string().trim();
                ((android.app.Activity) context).runOnUiThread(() -> {
                    if (res.equals("success")) {
                        savePlaceModelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, savePlaceModelList.size());
                        Toast.makeText(context, "Place removed from saved list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete place", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return savePlaceModelList.size();
    }
}

class SaveImageAdapter extends RecyclerView.ViewHolder {

    TextView item_txt_user_Save;
    ImageView item_Img_user_Save, item_saveIcon_Save;

    public SaveImageAdapter(@NonNull View itemView) {
        super(itemView);

        item_txt_user_Save = itemView.findViewById(R.id.item_txt_user_Save);
        item_Img_user_Save = itemView.findViewById(R.id.item_Img_user_Save);
        item_saveIcon_Save = itemView.findViewById(R.id.item_saveIcon_Save);
    }
}
