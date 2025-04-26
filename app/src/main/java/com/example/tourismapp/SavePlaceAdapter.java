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

public class SavePlaceAdapter extends RecyclerView.Adapter<SaveImageAdapter> {

    private Context context;
    private List<SavePlaceModel> savePlaceModelList;

    private String userId;
    private String username;

    public SavePlaceAdapter(Context context, List<SavePlaceModel> savePlace){
        this.context = context;
        this.savePlaceModelList = savePlace;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.userId = sharedPreferences.getString("user_id", "N/A");
        this.username = sharedPreferences.getString("username",username);
        if ("N/A".equals(this.username)) {
            Toast.makeText(context, "Error: username not found in shared preferences", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public SaveImageAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_save_place,parent,false);
        return new SaveImageAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveImageAdapter holder, int position) {
        SavePlaceModel savePlaceModel = savePlaceModelList.get(position);
        Glide.with(context).load(savePlaceModel.getImage()).into(holder.item_Img_user_Save);
        holder.item_txt_user_Save.setText(savePlaceModel.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("urlImage",savePlaceModel.getImage());
                intent.putExtra("name",savePlaceModel.getName());
                intent.putExtra("description",savePlaceModel.getDescription());
                intent.putExtra("category",savePlaceModel.getCategory());
                intent.putExtra("tags",savePlaceModel.getTags());
                intent.putExtra("exact_location",savePlaceModel.getExact_location());
                intent.putExtra("timing",savePlaceModel.getTiming());
                intent.putExtra("fees",savePlaceModel.getFees());
                intent.putExtra("contact",savePlaceModel.getContact());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savePlaceModelList.size();
    }
}

class SaveImageAdapter extends RecyclerView.ViewHolder{

    TextView item_txt_user_Save;
    ImageView item_Img_user_Save, item_saveIcon_Save;

    public SaveImageAdapter(@NonNull View itemView) {
        super(itemView);

        item_txt_user_Save = itemView.findViewById(R.id.item_txt_user_Save);
        item_Img_user_Save = itemView.findViewById(R.id.item_Img_user_Save);
        item_saveIcon_Save = itemView.findViewById(R.id.item_saveIcon_Save);
    }
}
