package com.example.dut_health_app;

public class UserProfileInfo {


    private String userID; // New field

    private String fullName, studentNumber, phoneNumber, nextOfKinName, nextOfKinPhoneNumber, address;

    public UserProfileInfo() {
        // Default constructor required for Firebase
    }

    public UserProfileInfo(String userID, String fullName, String studentNumber, String phoneNumber,
                           String nextOfKinName, String nextOfKinPhoneNumber, String address) {
        this.userID = userID;
        this.fullName = fullName;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.nextOfKinName = nextOfKinName;
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
        this.address = address;
    }


    // Getters and setters for all fields

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public String getNextOfKinPhoneNumber() {
        return nextOfKinPhoneNumber;
    }

    public void setNextOfKinPhoneNumber(String nextOfKinPhoneNumber) {
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
