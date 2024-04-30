package com.example.dut_health_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    TextInputEditText Email, Password, Password2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        Button LoginBtn= findViewById(R.id.Take_login);
        mAuth=FirebaseAuth.getInstance();
        Button SignUpBtn= findViewById(R.id.signup_btn);
        Email=findViewById(R.id.usernameEditText);
        Password=findViewById(R.id.userPasswordEditText);
        Password2=findViewById(R.id.userPassword2EditText);
        progressBar=findViewById(R.id.progressBar);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);
                String email,password,password2;
                email=String.valueOf(Email.getText());
                password=String.valueOf(Password.getText());
                password2=String.valueOf(Password2.getText());

                if (email.isEmpty()) {
                    Toast.makeText(SignUp.this, "Email field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Password field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password2.isEmpty()) {
                    Toast.makeText(SignUp.this, "Password confirmation field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(password2)) {
                    Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(view.GONE);
                                    Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, SetUpActivity.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(view.GONE);
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }
        });



        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login page
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}