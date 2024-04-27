package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetUpActivity extends AppCompatActivity {
    TextView Email;
    FirebaseUser user;
    FirebaseAuth auth;
    TextInputEditText fullName, studentNumber, phoneNumber, nextOfKinName, nextOfKinPhoneNumber, address;
    Button SubmitBtn;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_up);
        auth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.user_email);
        fullName = findViewById(R.id.user_Full_Name);
        studentNumber = findViewById(R.id.Student_number);
        phoneNumber = findViewById(R.id.phone_number);
        nextOfKinName = findViewById(R.id.next_of_kin_name);
        nextOfKinPhoneNumber = findViewById(R.id.next_of_kin_phone_number);
        address = findViewById(R.id.address);
        SubmitBtn = findViewById(R.id.submit_Btn);
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (user == null) {
            Intent intent = new Intent(SetUpActivity.this, Login.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent going back if the user is not logged in
        } else {
            Email.setText(user.getEmail());
        }
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    writeNewUser(user.getUid());
                } else {
                    // Handle the case where user is null
                    Toast.makeText(SetUpActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void writeNewUser(String userId) {
        String name = fullName.getText().toString().trim();
        String number = studentNumber.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String nextKinName = nextOfKinName.getText().toString().trim();
        String nextKinPhone = nextOfKinPhoneNumber.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String id=user.getUid();

        UserProfileInfo userProfileInfo = new UserProfileInfo(id,name, number, phone, nextKinName, nextKinPhone, addr);
        mDatabase.child("userInfo").child(userId).setValue(userProfileInfo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    // There was an error saving the data
                    Toast.makeText(SetUpActivity.this, "Information Uploaded "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(SetUpActivity.this, "Failed to upload information", Toast.LENGTH_SHORT).show();
                } else {
                    // Data saved successfully
                    Toast.makeText(SetUpActivity.this, "Information Uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetUpActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish(); // Finish the current activity after successful upload
                }
            }
        });
    }
}

