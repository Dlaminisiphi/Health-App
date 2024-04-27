package com.example.dut_health_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Reviews> mReviews;

    public ReviewsAdapter(List<Reviews> reviews) {
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reviews reviews = mReviews.get(position);
        holder.userEmail.setText(reviews.getUserEmail());
        holder.ratingBar.setRating(reviews.getRating());
        holder.reviewContent.setText(reviews.getReviewText());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Float.toString(reviews.getTimestamp()));
            String formattedDateTime = new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(date);
            holder.timestamp.setText(formattedDateTime);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date", e);
            // Assuming that reviews.getTimestamp() returns a Float value that represents a date or time:
            Date timestampDate = new Date(reviews.getTimestamp().longValue());
            String formattedDateTime = new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(timestampDate);
            holder.timestamp.setText(formattedDateTime);
        }



    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RatingBar ratingBar;
        public TextView reviewContent,userEmail;
        public TextView timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            reviewContent = itemView.findViewById(R.id.review_content);
            timestamp = itemView.findViewById(R.id.timestamp);
            userEmail=itemView.findViewById(R.id.rating_text);
            ratingBar.setIsIndicator(true);
        }
    }
}
