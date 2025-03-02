package com.example.tourismapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ImageViewHolder>{
    private Context context;
    private List<Model> imagelist;

    public Adapter(Context context,List<Model> imagelist){
        this.context = context;
        this.imagelist = imagelist;
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

        Glide.with(context).load(model.getImage()).into(holder.item_imageView);
        holder.item_txtImageName.setText(model.getName());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,DetailsActivity.class);
//                intent.putExtra("urlImage",model.getImage());
//                intent.putExtra("name",model.getName());
//                intent.putExtra("info",model.getInfo());
//
//                context.startActivity(intent);
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("urlImage",model.getImage());
                intent.putExtra("name",model.getName());
                intent.putExtra("description",model.getDescription());
                intent.putExtra("category",model.getCategory());
                intent.putExtra("tags",model.getTags());
                intent.putExtra("exact_location",model.getExact_location());
                intent.putExtra("timing",model.getTiming());
                intent.putExtra("fees",model.getFees());
                intent.putExtra("contact",model.getContact());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }
}

class ImageViewHolder extends RecyclerView.ViewHolder{
    TextView item_txtImageName;
    ImageView item_imageView;


    // CONSTRUCTOR
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        item_imageView = itemView.findViewById(R.id.item_imageView);
        item_txtImageName = itemView.findViewById(R.id.item_txtImageName);
    }
}
