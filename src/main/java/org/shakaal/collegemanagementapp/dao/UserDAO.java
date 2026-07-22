package org.shakaal.collegemanagementapp.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.shakaal.collegemanagementapp.database.DatabaseConnection;
import org.shakaal.collegemanagementapp.models.User;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {

    public User authenticate(String username, String password) {

        String sql = """
            SELECT *
            FROM users
            WHERE username = ?
            AND password_hash = ?
            AND status = 'ACTIVE'
            """;

        try
                (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, username);

            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));

                user.setUsername(resultSet.getString("username"));

                user.setPasswordHash(resultSet.getString("password_hash"));

                user.setFullName(resultSet.getString("full_name"));

                user.setRole(resultSet.getString("role"));

                user.setStatus(resultSet.getString("status"));

                user.setCreatedAt(LocalDate.parse(resultSet.getString("created_at").substring(0,10)));

                return user;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;
    }





    public boolean addUser(User user) {

        String sql = """
            INSERT INTO users
            (
                username,
                password_hash,
                full_name,
                role,
                status
            )

                VALUES
                (
                ?,?,?,?,?
                )
            """;

        try
                (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, user.getUsername());

            statement.setString(2, user.getPasswordHash());

            statement.setString(3, user.getFullName());

            statement.setString(4, user.getRole());

            statement.setString(5, user.getStatus());

            return statement.executeUpdate() > 0;

        }
        catch (SQLException e){

            e.printStackTrace();

            return false;
        }
    }



    public ObservableList<User> getAllUsers() {

        ObservableList<User> userList = FXCollections.observableArrayList();

        String sql = """
            SELECT *
            FROM users
            ORDER BY full_name
            """;

        try
                (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt("user_id"));

                user.setUsername(resultSet.getString("username"));

                user.setPasswordHash(resultSet.getString("password_hash"));

                user.setFullName(resultSet.getString("full_name"));

                user.setRole(resultSet.getString("role"));

                user.setStatus(resultSet.getString("status"));

                user.setCreatedAt(LocalDate.parse(resultSet.getString("created_at").substring(0, 10)));

                userList.add(user);

            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return userList;
    }




    public boolean deleteUser(int userId) {

        String sql = """
            DELETE FROM users
            WHERE user_id = ?
            """;

        try
                (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
