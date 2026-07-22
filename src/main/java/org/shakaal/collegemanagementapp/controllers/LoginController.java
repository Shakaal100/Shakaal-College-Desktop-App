package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;

import org.shakaal.collegemanagementapp.dao.UserDAO;
import org.shakaal.collegemanagementapp.models.User;
import org.shakaal.collegemanagementapp.session.Session;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;


    @FXML
    public void initialize() {

        loginButton.setOnAction(event -> handleLogin());

        usernameField.setOnAction(event -> passwordField.requestFocus());
        passwordField.setOnAction(event -> handleLogin());

    }



    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();

        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText(null);

            alert.setTitle("Missing Information");

            alert.setContentText("Please enter both username and password.");

            alert.showAndWait();

            return;

        }

        UserDAO userDAO = new UserDAO();

        User user = userDAO.authenticate(username, password);

        if (user != null) {

            Session.setCurrentUser(user);

            openDashboard();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);

            alert.setTitle("Login Failed");

            alert.setContentText("Invalid username or password.");

            alert.showAndWait();

        }

    }



    private void openDashboard() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/shakaal/collegemanagementapp/fxml/dashboard.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Remember current window state
            boolean maximized = stage.isMaximized();

            Scene scene = stage.getScene();

            scene.setRoot(root);

            stage.setTitle("College Management System");

            // Restore it
            stage.setMaximized(maximized);

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
