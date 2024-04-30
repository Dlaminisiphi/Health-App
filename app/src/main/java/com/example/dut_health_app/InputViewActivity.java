package com.example.dut_health_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class InputViewActivity extends AppCompatActivity {

    private EditText editTextDoctorSeen;
    private EditText editTextDiagnosis;
    private EditText editTextNextAppointment;
    private Button buttonSubmit;

    private DatabaseReference appointmentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_view);

        // Initialize Firebase Database reference
        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        // Get references to views
        editTextDoctorSeen = findViewById(R.id.editTextDoctorSeen);
        editTextDiagnosis = findViewById(R.id.editTextDiagnosis);
        editTextNextAppointment = findViewById(R.id.editTextNextAppointment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set OnClickListener for Submit Button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from EditText fields
                String doctorSeen = editTextDoctorSeen.getText().toString().trim();
                String diagnosis = editTextDiagnosis.getText().toString().trim();
                String nextAppointment = editTextNextAppointment.getText().toString().trim();
                String status = "Attended";

                // Get appointmentId and userId passed from previous Activity
                String appointmentId = getIntent().getStringExtra("appointmentId");
                String userId = getIntent().getStringExtra("userId");

                // Update appointment object in Firebase Realtime Database
                appointmentsRef.child(appointmentId).child("doctor").setValue(doctorSeen);
                appointmentsRef.child(appointmentId).child("notes").setValue(diagnosis);
                appointmentsRef.child(appointmentId).child("nextAppointment").setValue(nextAppointment);
                appointmentsRef.child(appointmentId).child("status").setValue(status);

                // Retrieve the user's email from the database
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(userId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserProfileInfo userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);
                            if (userProfileInfo != null) {
                                String userEmail = userProfileInfo.getUserEmail();
                                // Send the email using the retrieved email
                                new SendEmailTask(userEmail, doctorSeen, diagnosis, nextAppointment).execute();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors
                        Log.e("InputViewActivity", "Error retrieving user data: " + databaseError.getMessage());
                    }
                });

                // Finish the activity or perform any other action
                finish();
            }
        });
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private String assignedPerson;
        private String doctorSeen;
        private String nextAppointment;
        private String diagnosis;

        public SendEmailTask(String assignedPerson, String doctorSeen, String nextAppointment, String diagnosis) {
            this.assignedPerson = assignedPerson;
            this.doctorSeen = doctorSeen;
            this.nextAppointment = nextAppointment;
            this.diagnosis = diagnosis;
        }

        // Inside the SendEmailTask class
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Generate a unique reference number for the email
                UUID uuid = UUID.randomUUID();
                String referenceNumber = uuid.toString();

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // Create a new session with an authenticator
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("flaskschoolproject@gmail.com", "rwdgtyxxnyxlzmcf");
                    }
                });

                // Create a new message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("flaskschoolproject@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(assignedPerson));
                message.setSubject("Appointment Summary [" + referenceNumber + "]");

                // Compose the email body
                String emailBody =
                        "Thank you for visiting Dut Clinic. Below, you'll find details regarding your recent appointment:\n\n" +
                                "Doctor Attended By: " + doctorSeen + "\n" +
                                "Doctors notes: " + diagnosis + "\n" +
                                "Next Scheduled Appointment: " + nextAppointment + "\n\n" +
                                "Thank you for choosing Dut Clinic for your healthcare needs.\n\n" +
                                "Best regards,\n" +
                                "Dut Clinic";
                message.setText(emailBody);

                // Send the email
                Transport.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}

