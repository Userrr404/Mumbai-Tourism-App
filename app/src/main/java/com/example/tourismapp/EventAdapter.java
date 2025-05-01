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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        holder.txtEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsEventActivity.class);
            intent.putExtra("event_id", event.getId());
            intent.putExtra("event_name", event.getName());
            intent.putExtra("event_date", event.getDate());
            intent.putExtra("event_desc", event.getDescription());
            intent.putExtra("event_image","http://10.0.2.2/tourism/" + event.getImage());
            // Optionally pass image URL or path
            context.startActivity(intent);
        });

        holder.txtRemove.setOnClickListener(v -> {
            removeEventFromDatabase(event.getId(), position);
        });

    }

    private void removeEventFromDatabase(String id, int position) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2/tourism/db_delete_event.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ((Activity) context).runOnUiThread(() -> {
                        eventList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Event removed", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }


}

class EventViewHolder extends RecyclerView.ViewHolder {
    ImageView imgEvent;
    TextView txtEventName, txtEventDate, txtEventDesc, txtEdit, txtRemove;


    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        imgEvent = itemView.findViewById(R.id.imgEvent);
        txtEventName = itemView.findViewById(R.id.txtEventName);
        txtEventDate = itemView.findViewById(R.id.txtEventDate);
        txtEventDesc = itemView.findViewById(R.id.txtEventDesc);
        txtEdit = itemView.findViewById(R.id.txtEdit);
        txtRemove = itemView.findViewById(R.id.txtRemove);
    }
}
