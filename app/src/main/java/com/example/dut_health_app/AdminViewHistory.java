package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AdminViewHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_history);

        // Initialize RecyclerView and AppointmentAdapter
        recyclerView = findViewById(R.id.Bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList, this);
        recyclerView.setAdapter(appointmentAdapter);

        // Retrieve selected user's userId from intent extra
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SELECTED_USER_ID")) {
            String selectedUserId = intent.getStringExtra("SELECTED_USER_ID");
            if (selectedUserId != null) {
                // Use selected user's userId to query database for appointments
                fetchAppointmentsForUser(selectedUserId);
            }
        }
    }

    // Method to fetch appointments for a given user
    private void fetchAppointmentsForUser(String userId) {
        // Perform database query to fetch appointments for the user
        // Example: Firebase Realtime Database query
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        appointmentsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    appointmentList.add(appointment);
                }
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database query cancellation or error
            }
        });
    }
}
