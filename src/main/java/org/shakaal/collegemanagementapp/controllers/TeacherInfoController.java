package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

import org.shakaal.collegemanagementapp.models.Teacher;
import org.shakaal.collegemanagementapp.storage.AppDataManager;

public class TeacherInfoController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label teacherNameLabel;

    @FXML
    private Label teacherIdLabel;

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
    private Label specializationLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Button closeButton;


    @FXML
    private void initialize() {

        closeButton.setOnAction(event -> {

            Stage stage = (Stage) closeButton.getScene().getWindow();

            stage.close();
        });
    }


    public void setTeacher(Teacher teacher) {

        // =====================================================
        // TEACHER ID
        // =====================================================

        teacherIdLabel.setText("Teacher ID: " + teacher.getTeacherID());


        // =====================================================
        // STATUS
        // =====================================================

        String status = teacher.getStatus();

        statusLabel.setText("● " + status);


        // Remove previous status styles
        statusLabel.getStyleClass().removeAll("status-active", "status-inactive");


        // Apply the correct status style
        if ("Active".equalsIgnoreCase(status)) {

            statusLabel.getStyleClass().add("status-active");

        } else {

            statusLabel.getStyleClass().add("status-inactive");
        }


        // =====================================================
        // TEACHER NAME
        // =====================================================

        fullNameLabel.setText(teacher.getFullName());

        teacherNameLabel.setText(teacher.getFullName());


        // =====================================================
        // PERSONAL INFORMATION
        // =====================================================

        genderLabel.setText(teacher.getGender());


        // =====================================================
        // CONTACT INFORMATION
        // =====================================================

        phoneLabel.setText(teacher.getPhone());

        emailLabel.setText(teacher.getEmail());


        // =====================================================
        // PROFESSIONAL INFORMATION
        // =====================================================

        specializationLabel.setText(teacher.getSpecialization());

        salaryLabel.setText(String.valueOf(teacher.getSalary()));


        // =====================================================
        // PROFILE PICTURE
        // =====================================================

        loadTeacherPicture(teacher);
    }


    private void loadTeacherPicture(Teacher teacher) {

        // Clear any previously displayed picture
        profileImageView.setImage(null);

        System.out.println("=================================");
        System.out.println("Teacher ID: " + teacher.getTeacherID());
        System.out.println("Picture path from DB: [" + teacher.getPicturePath() + "]");
        System.out.println(
                "Teacher pictures folder: "
                        + AppDataManager.getTeacherPicturesFolder().getAbsolutePath()
        );

        if (teacher.getPicturePath() == null
                || teacher.getPicturePath().isBlank()) {

            System.out.println("❌ Picture path is NULL or BLANK");
            return;
        }

        File pictureFile = new File(
                AppDataManager.getTeacherPicturesFolder(),
                teacher.getPicturePath()
        );

        System.out.println(
                "Looking for picture: "
                        + pictureFile.getAbsolutePath()
        );

        System.out.println(
                "Exists: "
                        + pictureFile.exists()
        );

        if (!pictureFile.exists()) {

            System.out.println("❌ PICTURE FILE NOT FOUND");

            return;
        }

        System.out.println("✅ PICTURE FILE FOUND");

        Image image = new Image(
                pictureFile.toURI().toString()
        );

        System.out.println(
                "Image error: "
                        + image.isError()
        );

        if (image.isError()) {

            System.out.println(
                    "❌ JavaFX failed to load the image"
            );

            return;
        }

        profileImageView.setImage(image);

        System.out.println("✅ IMAGE SET INTO IMAGEVIEW");
        System.out.println("=================================");
    }
}
