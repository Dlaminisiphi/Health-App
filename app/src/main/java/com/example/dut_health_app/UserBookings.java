package com.example.dut_health_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBookings extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    UserBookingAdaptor userBookingAdaptor;
    ArrayList<Appointment> list;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_bookings);
        recyclerView = findViewById(R.id.Bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance().getReference("appointments");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        userBookingAdaptor = new UserBookingAdaptor(this, list);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear(); // Clear the list before adding new appointments
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Appointment appointment = dataSnapshot.getValue(Appointment.class);
                        // Check if the appointment belongs to the current user and its status is "not seen"
                        if (appointment != null &&
                                appointment.getUserId().equals(currentUserId) &&
                                appointment.getStatus().equals("not seen")) {
                            list.add(appointment);
                        }
                    }
                    // Notify the adapter that the data has changed
                    userBookingAdaptor.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

    }
}
