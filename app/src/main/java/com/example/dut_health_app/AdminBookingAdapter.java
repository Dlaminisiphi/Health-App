package com.example.dut_health_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import java.util.List;
public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.AdminBookingViewHolder> {

    private Context mContext;
    private List<Appointment> mAppointments;
    private List<UserProfileInfo> mUserProfiles;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment, UserProfileInfo userProfile);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public AdminBookingAdapter(Context context, List<Appointment> appointments, List<UserProfileInfo> userProfiles) {
        this.mContext = context;
        this.mAppointments = appointments;
        this.mUserProfiles = userProfiles;
    }

    @NonNull
    @Override
    public AdminBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_bookings, parent, false);
        return new AdminBookingViewHolder(view, mListener, mAppointments, mUserProfiles);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookingViewHolder holder, int position) {
        // Check if position is within bounds of both lists
        if (position < mAppointments.size() && position < mUserProfiles.size()) {
            Appointment appointment = mAppointments.get(position);
            UserProfileInfo userProfile = mUserProfiles.get(position);

            holder.tvName.setText("Patient Name: " + userProfile.getFullName());
            holder.tvReasonForVisit.setText("Appointment Reason: " + appointment.getReason());
            holder.tvStudentNumber.setText("Patient Student Number: " + userProfile.getStudentNumber());
            holder.tvAppointmentDate.setText("Appointment Date and Time: " + appointment.getDate() + " " + appointment.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public static class AdminBookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvReasonForVisit, tvStudentNumber, tvAppointmentDate;
        CardView cardView;

        public AdminBookingViewHolder(@NonNull View itemView, final OnItemClickListener listener,
                                      List<Appointment> appointments, List<UserProfileInfo> userProfiles) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvReasonForVisit = itemView.findViewById(R.id.tvReasonForVisit);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            cardView = itemView.findViewById(R.id.patientCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(appointments.get(position), userProfiles.get(position));
                        }
                    }
                }
            });
        }
    }

}

