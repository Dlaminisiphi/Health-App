package com.example.dut_health_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserBookingHistoryAdapter extends RecyclerView.Adapter<UserBookingHistoryAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private Context context;

    public UserBookingHistoryAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.tvDate.setText("Date: " + appointment.getDate());
        holder.tvTime.setText("Time: " + appointment.getTime());
        holder.tvReason.setText("Appointment Type: " + appointment.getReason());
        holder.tvDoctor.setText("Doctor Attended: " + appointment.getDoctor());
        holder.tvStatus.setText("Status: " + appointment.getStatus());
        holder.tvNotes.setText("Doctor Notes: " + appointment.getNotes());
        holder.tvNextAppointment.setText("Next Appointment Date: " + appointment.getNextAppointment());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvReason, tvDoctor, tvStatus, tvNotes, tvNextAppointment;
        CardView appointmentCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvDoctor = itemView.findViewById(R.id.tv_doctor);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            tvNextAppointment = itemView.findViewById(R.id.tv_next_appointment);
            appointmentCard = itemView.findViewById(R.id.appointment_card);
        }
    }
}
