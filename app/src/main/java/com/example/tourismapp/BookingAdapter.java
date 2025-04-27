package com.example.tourismapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder>{

    Context context;
    List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.name.setText(booking.name);
        holder.email.setText(booking.email);
        holder.date.setText(booking.booking_date);
        holder.people.setText(String.valueOf(booking.number_of_people));
        holder.fees.setText("₹" + booking.fees);

        // Load image using Glide
        Glide.with(context).load(booking.image_path).into(holder.image);

        holder.deleteBtn.setOnClickListener(v -> {
            deleteBooking(booking.id, position);
        });

        holder.status.setText(booking.booking_status);

        if(booking.booking_status.equals("Pending")) {
            holder.status.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else if(booking.booking_status.equals("Approved")) {
            holder.status.setTextColor(Color.parseColor("#008000")); // Green
        } else if(booking.booking_status.equals("Cancelled")) {
            holder.status.setTextColor(Color.parseColor("#FF0000")); // Red
        }

        holder.approveBtn.setOnClickListener(v -> {
            updateBookingStatus(booking.id, "Approved", position);
        });

        holder.cancelBtn.setOnClickListener(v -> {
            updateBookingStatus(booking.id, "Cancelled", position);
        });


    }

    private void updateBookingStatus(int id, String status, int position) {
        if (position < 0 || position >= bookingList.size()) {
            // Ensure the position is valid
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .add("booking_status", status)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.0.101/tourism/admin_api/db_update_booking_status.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().string().equals("success")) {
                        ((Activity) context).runOnUiThread(() -> {
                            // Only proceed if the position is still valid in the updated list
                            if (position >= 0 && position < bookingList.size()) {
                                bookingList.get(position).booking_status = status;
                                notifyItemChanged(position);
                                Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void deleteBooking(int id, int position) {
        if (position < 0 || position >= bookingList.size()) {
            // Ensure the position is valid
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.0.101/tourism/admin_api/db_delete_booking.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body().string().equals("success")) {
                    ((Activity) context).runOnUiThread(() -> {
                        // Only remove if position is valid
                        if (position >= 0 && position < bookingList.size()) {
                            bookingList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Booking deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}

class BookingViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView name, email, date, people, fees;
    Button deleteBtn;
    TextView status;
    Button approveBtn, cancelBtn;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        email = itemView.findViewById(R.id.email);
        date = itemView.findViewById(R.id.date);
        people = itemView.findViewById(R.id.people);
        fees = itemView.findViewById(R.id.fees);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        status = itemView.findViewById(R.id.status);
        approveBtn = itemView.findViewById(R.id.approveBtn);
        cancelBtn = itemView.findViewById(R.id.cancelBtn);
    }
}
