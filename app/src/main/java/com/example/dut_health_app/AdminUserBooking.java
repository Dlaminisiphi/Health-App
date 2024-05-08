package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminUserBooking extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminBookingAdapter adapter;
    private List<Appointment> appointments;
    private List<UserProfileInfo> userProfiles;
    private DatabaseReference appointmentsRef;
    private DatabaseReference userProfilesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_booking);

        recyclerView = findViewById(R.id.Bookings);
        appointments = new ArrayList<>();
        userProfiles = new ArrayList<>();
        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");
        userProfilesRef = FirebaseDatabase.getInstance().getReference().child("userInfo");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdminBookingAdapter(this, appointments, userProfiles);
        recyclerView.setAdapter(adapter);

        // Attach listeners to retrieve data from Firebase Database
// Attach listeners to retrieve data from Firebase Database
        appointmentsRef.orderByChild("status").equalTo("not seen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointments.add(appointment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });



        userProfilesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfiles.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserProfileInfo userProfile = snapshot.getValue(UserProfileInfo.class);
                    userProfiles.add(userProfile);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Set OnClickListener for the items in the adapter
        adapter.setOnItemClickListener(new AdminBookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment, UserProfileInfo userProfile) {
                // Start InputViewActivity and pass necessary data
                Intent intent = new Intent(AdminUserBooking.this, InputViewActivity.class);
                intent.putExtra("appointmentId", appointment.getId());
                intent.putExtra("userId", userProfile.getUserID());
                startActivity(intent);
            }
        });
    }
}
