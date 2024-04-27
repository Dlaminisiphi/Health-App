package com.example.dut_health_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserBookingAdaptor extends RecyclerView.Adapter<UserBookingAdaptor.MyViewHolder> {

    Context context;
    ArrayList<Appointment> list;

    public UserBookingAdaptor(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_booking_adaptor,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment appointment= list.get(position);
        holder.Apointment.setText("Reason for Appointment: "+appointment.getReason());
        holder.Date.setText("Time and Date: "+appointment.getDate()+" "+appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Apointment, Date;
        Button Cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Apointment=itemView.findViewById(R.id.reasonTextView);
            Date=itemView.findViewById(R.id.dateAndTimeTextView);
            Cancel=itemView.findViewById(R.id.cancelButton);

            // Set OnClickListener for the cancel button
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Appointment appointment = list.get(position);
                        updateStatusToCancel(appointment);
                        list.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });

        }
    }

    // Method to update status to "cancel" in Firebase
    private void updateStatusToCancel(Appointment appointment) {
        // Update the status of the appointment object
        appointment.setStatus("cancel");

        // Update the status in Firebase
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointment.getId());
        appointmentRef.child("status").setValue("cancel");


    }
}
