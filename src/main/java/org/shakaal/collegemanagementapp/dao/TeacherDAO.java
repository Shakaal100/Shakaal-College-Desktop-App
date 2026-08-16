package org.shakaal.collegemanagementapp.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.shakaal.collegemanagementapp.database.DatabaseClass;
import org.shakaal.collegemanagementapp.models.Teacher;
import org.shakaal.collegemanagementapp.session.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherDAO {


    //********************************************************
    // ************ Get All Teachers method   ****************
    // *******************************************************

    public ObservableList<Teacher> getAllTeachers() {

        ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM teachers";

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery())

        {
            while (resultSet.next()){
                Teacher teacher = new Teacher();

                teacher.setTeacherID(resultSet.getInt("teacher_id"));
                teacher.setFullName(resultSet.getString("full_name"));
                teacher.setGender(resultSet.getString("gender"));
                teacher.setPhone(resultSet.getString("phone"));
                teacher.setEmail(resultSet.getString("email"));
                teacher.setSpecialization(resultSet.getString("specialization"));
                teacher.setSalary(resultSet.getDouble("salary"));
                teacher.setStatus(resultSet.getString("status"));

                teacherList.add(teacher);
            }
        }

        catch (SQLException e ) {
            e.printStackTrace();
        }

        return teacherList;

    }

    // *********************  ADD TEACHER METHOD ***************

    public boolean addTeacher(Teacher teacher) {

        String sql = """
                    INSERT INTO teachers (full_name, gender, phone, email, specialization, salary)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

        try
                (Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql))

        {
            statement.setString(1, teacher.getFullName());
            statement.setString(2, teacher.getGender());
            statement.setString(3, teacher.getPhone());
            statement.setString(4, teacher.getEmail());
            statement.setString(5, teacher.getSpecialization());
            statement.setDouble(6, teacher.getSalary());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }

        catch ( SQLException e ) {
             e.printStackTrace();

             return false;
        }

    }

    // ************************ UPDATE TEACHER *****************

    public boolean updateTeacher(Teacher teacher) {
        String sql = """
                    UPDATE teachers
                    SET full_name = ?, gender = ?, phone = ?, email = ?, specialization = ?, salary = ?
                    WHERE teacher_id = ?
                """;

        try
                (Connection connection = DatabaseClass.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, teacher.getFullName());
            statement.setString(2, teacher.getGender());
            statement.setString(3, teacher.getPhone());
            statement.setString(4, teacher.getEmail());
            statement.setString(5, teacher.getSpecialization());
            statement.setDouble(6, teacher.getSalary());

            // Last Parameter identifies WHICH teacher to update

            statement.setInt(7, teacher.getTeacherID());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    //********************************************************
    // ******SEARCH TEACHERS, WITH FILTERS LOADING ************
    // *******************************************************


    public ObservableList<Teacher> searchTeachers(
            String keyword,
            String gender,
            String status) {

        ObservableList<Teacher> teacherList =
                FXCollections.observableArrayList();

        StringBuilder sql = new StringBuilder("""
            SELECT *
            FROM teachers
            WHERE
            (
                teacher_id LIKE ?
                OR full_name LIKE ?
                OR phone LIKE ?
                OR email LIKE ?
                OR specialization LIKE ?
            )
            """);

        if (!"All Genders".equals(gender)) {

            sql.append(" AND gender = ?");
        }

        if (!"All Status".equals(status)) {

            sql.append(" AND status = ?");
        }

        try (
                Connection connection = DatabaseClass.getConnection();

                PreparedStatement statement = connection.prepareStatement(sql.toString())
        ) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);
            statement.setString(3, searchKeyword);
            statement.setString(4, searchKeyword);
            statement.setString(5, searchKeyword);

            int parameterIndex = 6;

            if (!"All Genders".equals(gender)) {

                statement.setString(parameterIndex++, gender);
            }

            if (!"All Status".equals(status)) {

                statement.setString(parameterIndex++, status);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Teacher teacher = new Teacher();

                teacher.setTeacherID(resultSet.getInt("teacher_id"));

                teacher.setFullName(resultSet.getString("full_name"));

                teacher.setGender(resultSet.getString("gender"));

                teacher.setPhone(resultSet.getString("phone"));

                teacher.setEmail(resultSet.getString("email"));

                teacher.setSpecialization(resultSet.getString("specialization"));

                teacher.setSalary(resultSet.getDouble("salary"));

                teacher.setStatus(resultSet.getString("status"));

                teacherList.add(teacher);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return teacherList;
    }


    //********************************************************
    // ************ UPDATE STATUS  **************************
    // *******************************************************

    public boolean updateTeacherStatus(int teacherId, String status) {

        String sql = """
        UPDATE teachers
        SET status = ?
        WHERE teacher_id = ?
        """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);

            statement.setInt(2, teacherId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }

    //********************************************************
    // ************ GET TOTAL COUNT **************************
    // *******************************************************


    public int getTotalTeacherCount() {

        String sql = """
        SELECT COUNT(*)
        FROM teachers
        """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }

    public int getFemaleTeacherCount() {

        String sql = """
        SELECT COUNT(*)
        FROM teachers
        WHERE gender = 'Female'
        """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;
    }

    public int getMaleTeacherCount() {

        String sql = """
        SELECT COUNT(*)
        FROM teachers
        WHERE gender = 'Male'
        """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;
    }

    //********************************************************
    // ************ GET ACTIVE COUNT **************************
    // *******************************************************


    public int getActiveTeacherCount() {

        String sql = """
        SELECT COUNT(*)
        FROM teachers
        WHERE status = 'Active'
        """;

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
        return 0;
    }


    public boolean deleteTeacher(int teacherId) {

        String sql = "DELETE FROM teachers WHERE teacher_id = ?";

        try (
                Connection connection = DatabaseClass.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, teacherId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }
    }


}
