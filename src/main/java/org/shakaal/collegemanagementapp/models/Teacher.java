package org.shakaal.collegemanagementapp.models;

public class Teacher {
    private int teacherID;
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String specialization;
    private double salary;
    private String status;

    private String picturePath;

    // Empty constructor

    public Teacher(){

    }

    //Full Constructor

    public Teacher(int teacherID, String fullName, String gender, String phone, String email, String specialization, double salary, String status, String picturePath) {
        this.teacherID = teacherID;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.salary = salary;
        this.status = status;
        this.picturePath = picturePath;
    }


    // Getters


    public int getTeacherID() {
        return teacherID;
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

    public double getSalary() {
        return salary;
    }

    public String getStatus() {
        return status;
    }

    public String getPicturePath() {
        return picturePath;
    }

    // Setters


    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
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

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    // toString Method

    public String toString(){
        return fullName;
    }
}
