package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    Context context;
    ArrayList<EventModel> eventList;

    public EventAdapter(Context context, ArrayList<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        holder.txtEventName.setText(event.getName());
        holder.txtEventDate.setText(event.getDate());
        holder.txtEventDesc.setText(event.getDescription());

        Glide.with(context).load("http://10.0.2.2/tourism/" + event.getImage()).into(holder.imgEvent);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}

class EventViewHolder extends RecyclerView.ViewHolder {
    ImageView imgEvent;
    TextView txtEventName, txtEventDate, txtEventDesc;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        imgEvent = itemView.findViewById(R.id.imgEvent);
        txtEventName = itemView.findViewById(R.id.txtEventName);
        txtEventDate = itemView.findViewById(R.id.txtEventDate);
        txtEventDesc = itemView.findViewById(R.id.txtEventDesc);
    }
}
