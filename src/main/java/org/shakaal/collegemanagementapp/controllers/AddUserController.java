package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import org.shakaal.collegemanagementapp.dao.UserDAO;
import org.shakaal.collegemanagementapp.models.User;


public class AddUserController implements Initializable{

    // CONTROLS BROOOOOH  !!!

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private User selectedUser;

    //Constructors

    UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        roleComboBox.getItems().addAll("ADMIN", "REGISTRAR");

        saveButton.setOnAction(event->  saveUser());

        cancelButton.setOnAction(event -> closeWindow());

        //Forward The focus to next Field whenever enter is pressed

        usernameField.setOnAction(event -> passwordField.requestFocus());

        passwordField.setOnAction(event -> fullNameField.requestFocus());

        fullNameField.setOnAction(event -> roleComboBox.requestFocus());

       //     roleComboBox.setOnAction(event -> saveButton.requestFocus());
    }

    public void setUser(User user) {

        selectedUser = user;

        usernameField.setText(user.getUsername());

        passwordField.setText(user.getPasswordHash());

        fullNameField.setText(user.getFullName());

        roleComboBox.setValue(user.getRole());

        saveButton.setText("Update Student");
    }

    private void saveUser() {

        if (usernameField.getText().trim().isEmpty()) {

            showError("Username is required.");

            usernameField.requestFocus();

            return;
        }

        if (passwordField.getText().trim().isEmpty()) {

            showError("Password is required.");

            passwordField.requestFocus();

            return;
        }

        if (fullNameField.getText().trim().isEmpty()) {

            showError("Full name is required.");

            fullNameField.requestFocus();

            return;
        }

        if (roleComboBox.getValue() == null) {

            showError("Please select a role.");

            roleComboBox.requestFocus();

            return;
        }

        User user = new User();

        user.setUsername(usernameField.getText().trim());

        user.setPasswordHash(passwordField.getText().trim());

        user.setFullName(fullNameField.getText().trim());

        user.setRole(roleComboBox.getValue());

        user.setStatus("ACTIVE");

        UserDAO userDAO = new UserDAO();

        boolean success = userDAO.addUser(user);

        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Success");

            alert.setHeaderText(null);

            alert.setContentText("User added successfully.");

            alert.showAndWait();

            closeWindow();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");

            alert.setHeaderText(null);

            alert.setContentText("Failed to add user.");

            alert.showAndWait();

        }

    }

    private void closeWindow() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();
    }


    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Validation Error");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();

    }


}

