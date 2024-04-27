package com.example.dut_health_app;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;

public class AdminUserHistory extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserProfileInfo> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_history);

        // Initialize RecyclerView and UserAdapter
        recyclerView = findViewById(R.id.Bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this, this);
        recyclerView.setAdapter(userAdapter);

        // Retrieve list of users from the database
        // Example: Firebase Realtime Database query
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("userInfo");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserProfileInfo user = dataSnapshot.getValue(UserProfileInfo.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database query cancellation or error
            }
        });
    }

    @Override
    public void onUserClick(int position) {
        // Handle user click event
        UserProfileInfo selectedUser = userList.get(position);

        // Pass the selected user's userId to the AdminViewHistory activity
        Intent intent = new Intent(this, AdminViewHistory.class);
        intent.putExtra("SELECTED_USER_ID", selectedUser.getUserID());
        startActivity(intent);
    }
}



