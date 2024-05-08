package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    CardView Profile, LogOut, BookApointment, EmergencyServices, MyBookings, Review, SafetyTips, WriteARiview,MyHistory,Bot;
    TextView DashBoardName;
    FirebaseUser user;

    private DatabaseReference mDatabase;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        Profile = findViewById(R.id.Profile_btn);
        LogOut = findViewById(R.id.Log_Out_Btn);
        BookApointment = findViewById(R.id.Booking_Btn);
        DashBoardName = findViewById(R.id.dashboard_name);
        MyHistory=findViewById(R.id.My_History_Btn);
        EmergencyServices = findViewById(R.id.Emergency_Btn);
        MyBookings = findViewById(R.id.My_Bookings_Btn);
        Review = findViewById(R.id.Reviews_Btn);
        WriteARiview = findViewById(R.id.Review_Btn);
        SafetyTips = findViewById(R.id.Saftey_Btn);
        Bot=findViewById(R.id.bot_Btn);
        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve user information from the database
        mDatabase.child("userInfo").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserProfileInfo userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);
                    if (userProfileInfo != null) {
                        String userName = userProfileInfo.getFullName();
                        DashBoardName.setText(userName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        Bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MainActivity2.class);
                startActivity(intent);


            }
        });

        Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, AllReviews.class);
                startActivity(intent);


            }
        });

        MyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MyBookingHistory.class);
                startActivity(intent);


            }
        });


        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                startActivity(intent);

            }
        });

        WriteARiview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, WriteReview.class);
                startActivity(intent);

            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                finish();

            }
        });
        BookApointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ClinicBooking.class);
                startActivity(intent);

            }
        });

        SafetyTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, SafetyTips.class);
                startActivity(intent);

            }
        });

        EmergencyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, EmergencyServices.class);
                startActivity(intent);

            }
        });

        MyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, UserBookings.class);
                startActivity(intent);

            }
        });


    }
}
