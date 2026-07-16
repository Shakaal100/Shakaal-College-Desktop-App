package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button studentsButton;

    @FXML
    public void initialize() {

        loadPage("/org/shakaal/collegemanagementapp/fxml/dashboard-home.fxml");

        dashboardButton.setOnAction(e ->
                loadPage(
                        "/org/shakaal/collegemanagementapp/fxml/dashboard-home.fxml"));

        studentsButton.setOnAction(e ->
                loadPage("/org/shakaal/collegemanagementapp/fxml/students.fxml"));
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
