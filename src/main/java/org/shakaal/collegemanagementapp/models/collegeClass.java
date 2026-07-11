package org.shakaal.collegemanagementapp.models;

public class collegeClass {

    private int classId;
    private String className;
    private int courseId;
    private int teacherId;
    private String room;
    private String schedule;

    // Empty Constructor
    public collegeClass() {

    }

    // Full Constructor
    public collegeClass(int classId, String className, int courseId,
                        int teacherId, String room, String schedule) {
        this.classId = classId;
        this.className = className;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.room = room;
        this.schedule = schedule;
    }

    // Getters
    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getRoom() {
        return room;
    }

    public String getSchedule() {
        return schedule;
    }

    // Setters
    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return className;
    }
}