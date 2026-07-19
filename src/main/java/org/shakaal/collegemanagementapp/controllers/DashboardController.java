package org.shakaal.collegemanagementapp.controllers;
import org.shakaal.collegemanagementapp.models.User;
import org.shakaal.collegemanagementapp.session.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button studentsButton;

    @FXML
    public void initialize() {

        User currentUser = Session.getCurrentUser();

        if (currentUser != null) {

            welcomeLabel.setText("Welcome, " + currentUser.getFullName());

            roleLabel.setText("Role: " + currentUser.getRole());

        }

        loadPage("/org/shakaal/collegemanagementapp/fxml/dashboard-home.fxml");

        dashboardButton.setOnAction(e -> loadPage("/org/shakaal/collegemanagementapp/fxml/dashboard-home.fxml"));

        studentsButton.setOnAction(e -> loadPage("/org/shakaal/collegemanagementapp/fxml/students.fxml"));
    }



    private void loadPage(String fxmlFile) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

            Parent page = loader.load();

            contentArea.getChildren().setAll(page);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
