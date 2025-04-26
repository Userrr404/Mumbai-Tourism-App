package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder>{
    private Context context;
    private List<User> users;

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.users = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.emailTextView.setText(user.getEmail());
        holder.passwordTextView.setText(user.getPassword());
        holder.fullNameTextView.setText(user.getFull_name());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView usernameTextView;
    TextView emailTextView;
    TextView fullNameTextView, passwordTextView;

    public UserViewHolder(View itemView) {
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.textUsername);
        emailTextView = itemView.findViewById(R.id.textEmail);
        fullNameTextView = itemView.findViewById(R.id.textFullName);
        passwordTextView = itemView.findViewById(R.id.textPassword);
    }
}
