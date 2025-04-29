package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourismapp.R;
import com.example.tourismapp.Itinerary;

import java.util.ArrayList;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Itinerary> itineraryList;

    public ItineraryAdapter(Context context, ArrayList<Itinerary> itineraryList) {
        this.context = context;
        this.itineraryList = itineraryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_itinerary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Itinerary itinerary = itineraryList.get(position);

        holder.nameTextView.setText(itinerary.getName());
        holder.bookingDateTextView.setText("Date: " + itinerary.getBookingDate());
        holder.peopleTextView.setText("People: " + itinerary.getNumberOfPeople());

        Glide.with(context)
                .load(itinerary.getImagePath()) // Assuming full URL is stored
                .placeholder(R.drawable.default_person_img)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, bookingDateTextView, peopleTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewPlaceName);
            bookingDateTextView = itemView.findViewById(R.id.textViewBookingDate);
            peopleTextView = itemView.findViewById(R.id.textViewNumberOfPeople);
            imageView = itemView.findViewById(R.id.imageViewPlace);
        }
    }
}
