package com.example.dut_health_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.Calendar;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ClinicBooking extends AppCompatActivity {
    private static final String TAG = "ClinicBooking";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText otherReasonEditText;
    private Spinner reasonSpinner, timeSpinner;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_booking);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDisplayDate = findViewById(R.id.tvDate);
        reasonSpinner = findViewById(R.id.reason_spinner);
        timeSpinner = findViewById(R.id.time_spinner);
        submitButton = findViewById(R.id.submit_button);
        otherReasonEditText = findViewById(R.id.other_reason_edittext);

        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReason = parent.getItemAtPosition(position).toString();
                if (selectedReason.equals("Other")) {
                    otherReasonEditText.setVisibility(View.VISIBLE); // Show EditText for other reason
                } else {
                    otherReasonEditText.setVisibility(View.GONE); // Hide EditText for other reason
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ClinicBooking.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });
    }

    private void bookAppointment() {
        // Get current user UID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not logged in
            // You can handle this case as per your application logic
            return;
        }
        String userId = currentUser.getUid();

        // Get user input
        String date = mDisplayDate.getText().toString().trim();
        // Create appointment object with reason based on spinner selection
        String reason;
        if (reasonSpinner.getSelectedItem().toString().equals("Other")) {
            reason = otherReasonEditText.getText().toString().trim(); // Use text from otherReasonEditText
        } else {
            reason = reasonSpinner.getSelectedItem().toString(); // Use selected reason from spinner
        }
        String time = timeSpinner.getSelectedItem().toString();

        // Check if the selected slot is available
        isSlotAvailable(date, time, new SlotAvailabilityCallback() {
            @Override
            public void onSlotAvailable() {
                // Slot is available, proceed with booking

                // Set default status
                String status = "not seen";

                // Create appointment object
                // Generate a unique ID for the appointment
                String appointmentId = mDatabase.child("appointments").push().getKey();

// Create appointment object with empty doctor and notes, as admin will fill them later
                Appointment appointment = new Appointment(appointmentId, userId, date, reason, time, status, "", "", "");

// Save appointment to database
                mDatabase.child("appointments").child(appointmentId).setValue(appointment)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Appointment saved successfully
                                Toast.makeText(ClinicBooking.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();

                                // Send email to the current user
                                String assignedPerson = currentUser.getEmail();
                                new SendEmailTask(currentUser.getEmail(), date, time, reason).execute();
                                Intent intent = new Intent(ClinicBooking.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to save appointment
                                Toast.makeText(ClinicBooking.this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onSlotUnavailable() {
                // Slot is not available, inform the user or take appropriate action
                Toast.makeText(ClinicBooking.this, "The selected slot is already booked. Please choose another slot.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isSlotAvailable(String date, String time, SlotAvailabilityCallback callback) {
        // Construct the query to check for appointments at the selected date and time
        Query query = mDatabase.child("appointments")
                .orderByChild("date")
                .equalTo(date);

        // Perform the query
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if any appointments exist for the selected date
                if (dataSnapshot.exists()) {
                    // Appointments exist for the selected date
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve appointment details
                        String appointmentTime = snapshot.child("time").getValue(String.class);
                        String appointmentStatus = snapshot.child("status").getValue(String.class);
                        // Check if the appointment time matches the selected time and status is "not seen"
                        if (appointmentTime.equals(time) && appointmentStatus.equals("not seen")) {
                            // Appointment exists for the selected date, time, and status is "not seen"
                            // Invoke the callback with availability status
                            callback.onSlotUnavailable();
                            return;
                        }
                    }
                    // No appointments with status "not seen" exist for the selected time
                    // Invoke the callback with availability status
                    callback.onSlotAvailable();
                } else {
                    // No appointments exist for the selected date
                    // Invoke the callback with availability status
                    callback.onSlotAvailable();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors or interruptions in the database operation
                // For simplicity, you can treat it as if the slot is not available
                callback.onSlotUnavailable();
            }
        });

    }

    // Define a callback interface
    interface SlotAvailabilityCallback {
        void onSlotAvailable();
        void onSlotUnavailable();
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private String assignedPerson;
        private String date;
        private String time;
        private String reason;

        public SendEmailTask(String assignedPerson, String date, String time, String reason) {
            this.assignedPerson = assignedPerson;
            this.date = date;
            this.time = time;
            this.reason = reason;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
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
                message.setSubject("Appointment Confirmation [" + referenceNumber + "]");
                message.setText("Hello,\n" +
                        "Your appointment has been successfully booked!\n\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time + "\n" +
                        "Reason: " + reason + "\n\n" +
                        "Thank you for choosing our service. We look forward to seeing you.");

                Transport.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(ClinicBooking.this, "Appointment confirmation email sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ClinicBooking.this, "Failed to send appointment confirmation email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
