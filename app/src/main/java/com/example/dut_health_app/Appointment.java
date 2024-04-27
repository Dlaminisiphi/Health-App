package com.example.dut_health_app;

public class Appointment {
    private String id;
    private String userId;
    private String date;
    private String reason;
    private String time;
    private String doctor;
    private String notes;
    private String nextAppointment;
    private String status; // New field for status

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String id, String userId, String date, String reason, String time, String status, String doctor, String notes, String nextAppointment) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.reason = reason;
        this.time = time;
        this.status = status != null ? status : "not seen"; // Default status if null
        this.doctor = doctor != null ? doctor : "N/A";
        this.notes = notes != null ? notes : "N/A";
        this.nextAppointment = nextAppointment != null ? nextAppointment : "N/A";
    }

    // Getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(String nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}