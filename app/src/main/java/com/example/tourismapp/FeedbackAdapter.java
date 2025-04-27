package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.tourismapp.R;
import com.example.tourismapp.Feedback;

import java.util.List;

public class FeedbackAdapter extends BaseAdapter {

    private Context context;
    private List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @Override
    public int getCount() {
        return feedbackList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return feedbackList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        }

        // Get the feedback item
        Feedback feedback = feedbackList.get(position);

        // Bind data to the views
        TextView textUsername = convertView.findViewById(R.id.textUsername);
        TextView textFeedback = convertView.findViewById(R.id.textFeedback);
        TextView textCreatedAt = convertView.findViewById(R.id.textCreatedAt);

        textUsername.setText(feedback.getUsername());
        textFeedback.setText(feedback.getUser_feed());
        textCreatedAt.setText(feedback.getCreated_At());

        return convertView;
    }
}
