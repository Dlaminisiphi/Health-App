package com.example.dut_health_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button SignUpBtn, LoginBtn;
    TextInputEditText Email, Password;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Assuming EdgeToEdge is a utility class for UI layout
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);

        SignUpBtn = findViewById(R.id.signUp_);
        LoginBtn = findViewById(R.id.Login_Btn);
        Email = findViewById(R.id.usernameEditText);
        Password = findViewById(R.id.userPasswordEditText);
        progressBar = findViewById(R.id.progressBar);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });
    }

    private void signInUser() {
        progressBar.setVisibility(View.VISIBLE);
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("Email field is empty!");
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            Password.setError("Password field is empty!");
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (email.equals("admin@admin.com") && password.equals("password")) {
            Intent intent = new Intent(Login.this, AdminDashboard.class);
            startActivity(intent);
            finish();
            progressBar.setVisibility(View.GONE);
        } else {
            performFirebaseSignIn(email, password);
        }
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(Login.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }

    private void performFirebaseSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }
}