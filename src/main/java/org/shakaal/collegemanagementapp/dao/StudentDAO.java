package org.shakaal.collegemanagementapp.dao;

import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ==========================================================
 * StudentDAO.java
 * ==========================================================

 * Data Access Object (DAO)

 * Responsible for all database operations related
 * to the Students table.

 * This class communicates directly with SQLite.

 * It DOES NOT know anything about JavaFX controls.

 * Author: Shakaal
 * Project: College Management System
 * ==========================================================
 */

public class StudentDAO {

    // =====================================================
    // INSERT STUDENT
    // =====================================================

    /**
     * Saves a new student into the database.
     *
     * @param student Student object
     * @return true if successful
     */
    public boolean addStudent(Student student) {

        String sql = """
        INSERT INTO students
        (first_name, last_name, gender, registered_date, phone, email, address, course_id, status)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {

            statement.setString(1, student.getFirstName());

            statement.setString(2, student.getLastName());

            statement.setString(3, student.getGender());

            // LocalDate ---> String
            statement.setString(4, student.getRegisteredDate().toString());

            statement.setString(5, student.getPhone());

            statement.setString(6, student.getEmail());

            statement.setString(7, student.getAddress());

            statement.setInt(8, student.getCourseId());

            statement.setString(9, student.getStatus());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        }

        catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }
    /**=====================================================
    // GET ALL STUDENTS
    // =====================================================

    /**
     * Retrieves all students from the database.
     *
     * @return ObservableList containing all students.
     */
    public ObservableList<Student> getAllStudents() {

        ObservableList<Student> studentList = FXCollections.observableArrayList();

        String sql = """
                        SELECT
                            s.student_id,
                            s.first_name,
                            s.last_name,
                            s.gender,
                            s.registered_date,
                            s.phone,
                            s.email,
                            s.address,
                            s.course_id,
                            c.course_name,
                            s.status
                        FROM students s
                        INNER JOIN courses c
                        ON s.course_id = c.course_id
                        """;

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Student student = new Student();

                student.setStudentId(resultSet.getInt("student_id"));

                student.setFirstName(resultSet.getString("first_name"));

                student.setLastName(resultSet.getString("last_name"));

                student.setGender(resultSet.getString("gender"));

                String registeredDate = resultSet.getString("registered_date");

                if (registeredDate != null) {
                    student.setRegisteredDate(LocalDate.parse(registeredDate));
                }

                student.setPhone(resultSet.getString("phone"));

                student.setEmail(resultSet.getString("email"));

                student.setAddress(resultSet.getString("address"));

                student.setCourseId(resultSet.getInt("course_id"));

                student.setCourseName(resultSet.getString("course_name"));

                student.setStatus(resultSet.getString("status"));

                studentList.add(student);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return studentList;

    }

    // =====================================================
// UPDATE STUDENT
// =====================================================

    /**
     * Updates an existing student in the database.
     *
     * @param student Student object containing the updated data.
     * @return true if the update was successful, otherwise false.
     */
    public boolean updateStudent(Student student) {

        String sql = """
            UPDATE students
            SET first_name = ?, last_name = ?, gender = ?, registered_date = ?,
                phone = ?, email = ?, address = ?, course_id = ?, status = ?
            WHERE student_id = ?
            """;

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getGender());
            statement.setString(4, student.getRegisteredDate().toString());
            statement.setString(5, student.getPhone());
            statement.setString(6, student.getEmail());
            statement.setString(7, student.getAddress());
            statement.setInt(8, student.getCourseId());
            statement.setString(9, student.getStatus());

            // The last parameter identifies WHICH student to update
            statement.setInt(10, student.getStudentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    // =====================================================
// DELETE STUDENT
// =====================================================

    /**
     * Deletes a student from the database.
     *
     * @param studentId ID of the student to delete.
     * @return true if the student was deleted successfully,
     *         otherwise false.
     */
    public boolean deleteStudent(int studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }



    // =====================================================
// SEARCH STUDENTS
// =====================================================

    /**
     * Searches students by first name, last name,
     * phone or email.

     * @param keyword Search keyword.
     * @return ObservableList of matching students.
     */
    public ObservableList<Student> searchStudents(
            String keyword,
            String course,
            String gender,
            String status) {

        ObservableList<Student> studentList = FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder("""
                SELECT students.*,
                       courses.course_name
                FROM students
                LEFT JOIN courses
                    ON students.course_id = courses.course_id
                WHERE
                (
                    students.student_id LIKE ?
                    OR students.first_name LIKE ?
                    OR students.last_name LIKE ?
                    OR students.phone LIKE ?
                    OR students.email LIKE ?
                    OR courses.course_name LIKE ?
                )
            """);

        if (!"All Courses".equals(course)) {
            sql.append(" AND courses.course_name = ?");
        }

        if (!"All Genders".equals(gender)) {
            sql.append(" AND students.gender = ?");
        }

        if (!"All Status".equals(status)) {
            sql.append(" AND students.status = ?");
        }

        try (Connection connection = DatabaseClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);
            statement.setString(4, searchKeyword);
            statement.setString(5, searchKeyword);
            statement.setString(6, searchKeyword);

            int parameterIndex = 7;

            if (!"All Courses".equals(course)) {
                statement.setString(parameterIndex++, course);
            }

            if (!"All Genders".equals(gender)) {
                statement.setString(parameterIndex++, gender);
            }

            if (!"All Status".equals(status)) {
                statement.setString(parameterIndex++, status);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Student student = new Student();

                student.setStudentId(resultSet.getInt("student_id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setGender(resultSet.getString("gender"));
                student.setRegisteredDate(LocalDate.parse(resultSet.getString("registered_date")));
                student.setPhone(resultSet.getString("phone"));
                student.setEmail(resultSet.getString("email"));
                student.setAddress(resultSet.getString("address"));
                student.setCourseId(resultSet.getInt("course_id"));
                student.setCourseName(resultSet.getString("course_name"));
                student.setStatus(resultSet.getString("status"));

                studentList.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    // =====================================================
// GET STUDENT COUNT
// =====================================================

    /**
     * Returns the total number of students
     * in the database.
     *
     * @return total number of students.
     */
    public int getStudentCount() {

        String sql = "SELECT COUNT(*) FROM students";

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }


    public int getMaleStudentCount() {

        String sql = """
            SELECT COUNT(*)
            FROM students
            WHERE gender = 'Male'
            """;

        try (Connection connection = DatabaseClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }



    public int getFemaleStudentCount() {

        String sql = """
            SELECT COUNT(*)
            FROM students
            WHERE gender = 'Female'
            """;

        try (Connection connection = DatabaseClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }



    public int getActiveStudentCount() {

        String sql = """
            SELECT COUNT(*)
            FROM students
            WHERE status = 'Active'
            """;

        try (Connection connection = DatabaseClass.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }

    public boolean updateStudentStatus(int studentId, String status) {

        String sql = """
            UPDATE students
            SET status = ?
            WHERE student_id = ?
            """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

}
