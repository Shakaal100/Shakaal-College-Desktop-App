package org.shakaal.collegemanagementapp.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.shakaal.collegemanagementapp.database.DatabaseConnection;
import org.shakaal.collegemanagementapp.models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseDAO {

    public ObservableList<Course> getAllCourses() {

        ObservableList<Course> courseList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM courses";

        try {

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course course = new Course();

                course.setCourseId(resultSet.getInt("course_id"));

                course.setCourseName(resultSet.getString("course_name"));

                courseList.add(course);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return courseList;
    }
}
