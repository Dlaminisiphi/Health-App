package com.example.dut_health_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private EditText userNameEditText, phoneNumberEditText, nextOfKinNameEditText, nextOfKinPhoneNumberEditText, addressEditText;
    TextView Fullname, Email;
    private Button updateButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        Fullname = findViewById(R.id.full_name);
        Email = findViewById(R.id.email_add);
        phoneNumberEditText = findViewById(R.id.phone_number);
        nextOfKinNameEditText = findViewById(R.id.next_of_kin_name);
        nextOfKinPhoneNumberEditText = findViewById(R.id.next_of_kin_phone_number);
        addressEditText = findViewById(R.id.address);
        updateButton = findViewById(R.id.update_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve user information from the database
            mDatabase.child("userInfo").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User data found, populate the EditText fields
                        UserProfileInfo userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);
                        if (userProfileInfo != null) {
                            Fullname.setText(userProfileInfo.getFullName());
                            Email.setText(currentUser.getEmail());
                            phoneNumberEditText.setText(userProfileInfo.getPhoneNumber());
                            nextOfKinNameEditText.setText(userProfileInfo.getNextOfKinName());
                            nextOfKinPhoneNumberEditText.setText(userProfileInfo.getNextOfKinPhoneNumber());
                            addressEditText.setText(userProfileInfo.getAddress());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }

        updateButton.setOnClickListener(view -> {
            // Get updated user input
            String updatedPhoneNumber = phoneNumberEditText.getText().toString().trim();
            String updatedNextOfKinName = nextOfKinNameEditText.getText().toString().trim();
            String updatedNextOfKinPhoneNumber = nextOfKinPhoneNumberEditText.getText().toString().trim();
            String updatedAddress = addressEditText.getText().toString().trim();

            // Get current user ID
            FirebaseUser currentUser1 = mAuth.getCurrentUser();
            if (currentUser1 != null) {
                String userId = currentUser1.getUid();

                // Update user information in the database
                mDatabase.child("userInfo").child(userId).child("phoneNumber").setValue(updatedPhoneNumber);
                mDatabase.child("userInfo").child(userId).child("nextOfKinName").setValue(updatedNextOfKinName);
                mDatabase.child("userInfo").child(userId).child("nextOfKinPhoneNumber").setValue(updatedNextOfKinPhoneNumber);
                mDatabase.child("userInfo").child(userId).child("address").setValue(updatedAddress);
            }
        });
    }
}

