package org.shakaal.collegemanagementapp.models;

public class Course {
    private int courseId;

    private String courseCode;

    private String courseName;

    private String duration;

    private String schedule;

    private double courseFee;

    private int studentsCount;

    private String status;

    private String courseInfoPath;


    //Empty constructor
    public Course(){

    }

    // ******  full constructor   *********

    public Course(int courseId, String courseCode, String courseName, String duration, String schedule, double courseFee, int studentsCount,  String status, String courseInfoPath) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.duration = duration;
        this.schedule = schedule;
        this.courseFee = courseFee;
        this.studentsCount = studentsCount;
        this.status = status;
        this.courseInfoPath = courseInfoPath;
    }

    // ***** GETTERS AND SETTERS *******


    public int getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDuration() {
        return duration;
    }

    public String getSchedule() {
        return schedule;
    }

    public double getCourseFee() {
        return courseFee;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public String getStatus() {
        return status;
    }

    public String getCourseInfoPath() {
        return courseInfoPath;
    }


    // ****** SETTERS ******


    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCourseInfoPath(String courseInfoPath) {
        this.courseInfoPath = courseInfoPath;
    }

    @Override
    public String toString() {
        return courseName;
    }
}
