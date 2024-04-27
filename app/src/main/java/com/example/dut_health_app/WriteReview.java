package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteReview extends AppCompatActivity {

    RatingBar ratingBar;
    EditText reviewText;
    TextView rateCount, showRating;
    float rateValue;
    String temp;
    Button submitButton;
    DatabaseReference databaseRef;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        rateCount = findViewById(R.id.rateCount);
        ratingBar = findViewById(R.id.rating_bar);
        reviewText = findViewById(R.id.review_text);
        submitButton = findViewById(R.id.submit_button);
        showRating = findViewById(R.id.showRating);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("reviews");

        // Get the current user's email address
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = ratingBar.getRating();
                if (rateValue <= 1 && rateValue > 0)
                    rateCount.setText("Bad" + rateValue + "/5");
                else if (rateValue <= 2 && rateValue > 1)
                    rateCount.setText("Okay" + rateValue + "/5");
                else if (rateValue <= 3 && rateValue > 2)
                    rateCount.setText("Good" + rateValue + "/5");
                else if (rateValue <= 4 && rateValue > 3)
                    rateCount.setText("Very Good" + rateValue + "/5");
                else if (rateValue <= 5 && rateValue > 4)
                    rateCount.setText("Exceptionally Good" + rateValue + "/5");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the rating and review text are not empty
                if (rateValue == 0 || reviewText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please provide a rating and a comment", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a Review object and populate it with the review data
                    Reviews reviews = new Reviews();
                    reviews.setRating(rateValue);
                    reviews.setReviewText(reviewText.getText().toString());
                    reviews.setTimestamp((float) System.currentTimeMillis());
                    reviews.setUserEmail(userEmail); // Set the user's email address

                    // Call setValue() on the DatabaseReference object to store the data
                    databaseRef.push().setValue(reviews, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                // Display an error message if the data could not be saved
                                Toast.makeText(getApplicationContext(), "Error: Data could not be saved.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Display a success message if the data was successfully saved
                                Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}