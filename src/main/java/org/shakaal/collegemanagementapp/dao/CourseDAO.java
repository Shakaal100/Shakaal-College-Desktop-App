package org.shakaal.collegemanagementapp.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.shakaal.collegemanagementapp.database.DatabaseConnection;
import org.shakaal.collegemanagementapp.models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CourseDAO {

    //    *************************************************
    //    ******** GET ALL COURSES  ***********************
    //   ***************************************************

    public ObservableList<Course> getAllCourses() {

        ObservableList<Course> courseList = FXCollections.observableArrayList();

        String sql = """
            SELECT
                c.*,
                COUNT(s.student_id) AS students_count
            FROM courses c
            LEFT JOIN students s
                ON c.course_id = s.course_id
            GROUP BY c.course_id
            ORDER BY c.course_id
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery())
        {

            while (resultSet.next()) {

                Course course = new Course();

                course.setCourseId(resultSet.getInt("course_id"));

                course.setCourseCode(resultSet.getString("course_code"));

                course.setCourseName(resultSet.getString("course_name"));

                course.setDuration(resultSet.getString("duration"));

                course.setSchedule(resultSet.getString("schedule"));

                course.setCourseFee(resultSet.getDouble("course_fee"));

                course.setStudentsCount(resultSet.getInt("students_count"));

                course.setStatus(resultSet.getString("status"));

                course.setCourseInfoPath(resultSet.getString("course_info_path"));

                courseList.add(course);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return courseList;

    }
    //    *************************************************
    //    ********  ADD COURSE  ***************************
    //   ***************************************************

    public boolean addCourse(Course course) {

        String sql = """
            INSERT INTO courses (
                course_code,
                course_name,
                duration,
                schedule,
                course_fee,
                status,
                course_info_path
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, course.getCourseCode());

            statement.setString(2, course.getCourseName());

            statement.setString(3, course.getDuration());

            statement.setString(4, course.getSchedule());

            statement.setDouble(5, course.getCourseFee());

            statement.setString(6, course.getStatus());

            statement.setString(7, course.getCourseInfoPath());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }

    //    *************************************************
    //    ********  UPDATE COURSE  ************************
    //   ***************************************************

    public boolean updateCourse(Course course) {

        String sql = """
            UPDATE courses
            SET
                course_code = ?,
                course_name = ?,
                duration = ?,
                schedule = ?,
                course_fee = ?,
                status = ?,
                course_info_path = ?
            WHERE course_id = ?
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, course.getCourseCode());

            statement.setString(2, course.getCourseName());

            statement.setString(3, course.getDuration());

            statement.setString(4, course.getSchedule());

            statement.setDouble(5, course.getCourseFee());

            statement.setString(6, course.getStatus());

            statement.setString(7, course.getCourseInfoPath());

            statement.setInt(8, course.getCourseId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }

    //    *************************************************
    //    ********TOTAL COURSES COUNT  ********************
    //   ***************************************************

    public int getTotalCourseCount() {

        String sql = """
            SELECT COUNT(*)
            FROM courses
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
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

    //    *************************************************
    //    ********AVAILABLE COURSES COUNT  ****************
    //   ***************************************************

    public int getAvailableCourseCount() {

        String sql = """
            SELECT COUNT(*)
            FROM courses
            WHERE status = 'Available'
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
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


    //    *************************************************
    //    ******** ARCHIVED COURSES COUNT  ********************
    //   ***************************************************

    public int getArchivedCourseCount() {

        String sql = """
            SELECT COUNT(*)
            FROM courses
            WHERE status = 'Archived'
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
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

    //    *************************************************
    //    ******** PIE CHART FEEDING METHOD  **************
    //   ***************************************************

    public ObservableList<PieChart.Data> getCoursePieChartData() {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String sql = """
            SELECT
                c.course_name,
                COUNT(s.student_id) AS total_students
            FROM courses c
            LEFT JOIN students s
                ON c.course_id = s.course_id
            WHERE c.status = 'Available'
            GROUP BY c.course_id, c.course_name
            ORDER BY total_students DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                pieChartData.add(
                        new PieChart.Data(
                                resultSet.getString("course_name"),
                                resultSet.getInt("total_students")
                        )
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return pieChartData;
    }

    //    *************************************************
    //    ******** STATUS FILTER METHOD  *****************
    //   ***************************************************


    public ObservableList<Course> filterCourses(String keyword, String status) {

        ObservableList<Course> courseList = FXCollections.observableArrayList();

        String sql = """
        SELECT
            c.*,
            COUNT(s.student_id) AS students_count
        FROM courses c
        LEFT JOIN students s
            ON c.course_id = s.course_id
        WHERE
            (c.course_name LIKE ? OR c.schedule LIKE ?)
            AND
            (? = 'All' OR c.status = ?)
        GROUP BY c.course_id
        ORDER BY c.course_id
        """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);

            statement.setString(3, status);
            statement.setString(4, status);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Course course = new Course();

                course.setCourseId(resultSet.getInt("course_id"));

                course.setCourseCode(resultSet.getString("course_code"));

                course.setCourseName(resultSet.getString("course_name"));

                course.setDuration(resultSet.getString("duration"));

                course.setSchedule(resultSet.getString("schedule"));

                course.setCourseFee(resultSet.getDouble("course_fee"));

                course.setStatus(resultSet.getString("status"));

                course.setCourseInfoPath(resultSet.getString("course_info_path"));

                course.setStudentsCount(resultSet.getInt("students_count"));

                courseList.add(course);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return courseList;

    }
}
