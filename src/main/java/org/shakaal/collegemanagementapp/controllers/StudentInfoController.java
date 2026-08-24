package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.io.File;

import org.shakaal.collegemanagementapp.models.Student;
import org.shakaal.collegemanagementapp.storage.AppDataManager;

public class StudentInfoController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label studentNameLabel;


    @FXML
    private Label studentIdLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label courseLabel;

    @FXML
    private Label registeredDateLabel;

    @FXML
    private Button closeButton;


    @FXML
    private void initialize() {

        closeButton.setOnAction(event -> {

            Stage stage = (Stage) closeButton.getScene().getWindow();

            stage.close();
        });
    }


    public void setStudent(Student student) {

        studentIdLabel.setText("Student ID: " + student.getStudentId());

        String status = student.getStatus();

        statusLabel.setText("● " + status);


// Remove previous status styles
        statusLabel.getStyleClass().removeAll("status-active", "status-inactive");


// Apply the correct style
        if ("Active".equalsIgnoreCase(status)) {

            statusLabel.getStyleClass().add("status-active");

        } else {

            statusLabel.getStyleClass().add("status-inactive");
        }

        fullNameLabel.setText(student.getFirstName() + " " + student.getLastName());

        studentNameLabel.setText(student.getFirstName() + " " + student.getLastName());

        genderLabel.setText(student.getGender());

        phoneLabel.setText(student.getPhone());

        emailLabel.setText(student.getEmail());

        addressLabel.setText(student.getAddress());

        courseLabel.setText(student.getCourseName());

        if (student.getRegisteredDate() != null) {

            registeredDateLabel.setText(student.getRegisteredDate().toString());

        } else {

            registeredDateLabel.setText("—");
        }


        loadStudentPicture(student);
    }


    private void loadStudentPicture(Student student) {

        // Clear any previously displayed picture
        profileImageView.setImage(null);

        // Student does not have a picture
        if (student.getPicturePath() == null || student.getPicturePath().isBlank()) {

            return;
        }

        // Find the picture using our clean AppDataManager storage
        File pictureFile = new File(AppDataManager.getStudentPicturesFolder(), student.getPicturePath());

        // Picture file is missing
        if (!pictureFile.exists()) {

            return;
        }

        // Load the original picture
        Image image = new Image(pictureFile.toURI().toString());

        profileImageView.setImage(image);
    }
}