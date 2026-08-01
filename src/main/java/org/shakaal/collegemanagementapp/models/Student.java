package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;

/**
 * ==========================================================
 * Student.java
 * ==========================================================

 * Model class representing one student.
 ==========================================================
 */

public class Student {

    // ===============================
    // Fields
    // ===============================

    private int studentId;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate registeredDate;

    private String phone;

    private String email;

    private String address;

    private int courseId;

    private String courseName;

    private String status;

    // ===============================
    // Empty Constructor
    // ===============================

    public Student() {
    }

    // ===============================
    // Full Constructor
    // ===============================

    public Student(int studentId, String firstName, String lastName, String gender, LocalDate registeredDate, String phone, String email, String address, int courseId, String courseName, String status) {

        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.registeredDate = registeredDate;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.courseId = courseId;
        this.courseName = courseName;
        this.status = status;
    }

    // ===============================
    // Getters & Setters
    // ===============================

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}