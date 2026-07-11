package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;

public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private int classId;
    private LocalDate enrollmentDate;

    // Empty Constructor
    public Enrollment() {

    }

    // Full Constructor
    public Enrollment(int enrollmentId, int studentId, int classId, LocalDate enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.classId = classId;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    // Setters
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
