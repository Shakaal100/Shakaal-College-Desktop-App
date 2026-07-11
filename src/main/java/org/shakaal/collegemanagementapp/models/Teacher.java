package org.shakaal.collegemanagementapp.models;

public class Teacher {

    private int teacherId;
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String specialization;

    // Empty Constructor
    public Teacher() {

    }

    // Full Constructor
    public Teacher(int teacherId, String fullName, String gender,
                   String phone, String email, String specialization) {
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
    }

    // Getters
    public int getTeacherId() {
        return teacherId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    // Setters
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
