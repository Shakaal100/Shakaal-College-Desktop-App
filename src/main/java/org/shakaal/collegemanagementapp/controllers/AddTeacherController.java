package org.shakaal.collegemanagementapp.controllers;


// =========================================================
// JAVAFX IMPORTS
// =========================================================

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;


// =========================================================
// PROJECT IMPORTS
// =========================================================

import org.shakaal.collegemanagementapp.dao.TeacherDAO;
import org.shakaal.collegemanagementapp.models.Teacher;
import org.shakaal.collegemanagementapp.storage.AppDataManager;


public class AddTeacherController implements Initializable {


    // =========================================================
    // FORM FIELDS
    // =========================================================

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField specializationField;

    @FXML
    private TextField salaryField;


    // =========================================================
    // PICTURE CONTROLS
    // =========================================================

    @FXML
    private ImageView profileImageView;

    @FXML
    private javafx.scene.layout.VBox picturePlaceholder;

    @FXML
    private Button choosePictureButton;

    @FXML
    private Button removePictureButton;

    @FXML
    private Label pictureFileNameLabel;


    // =========================================================
    // ACTION BUTTONS
    // =========================================================

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveTeacherButton;


    // =========================================================
    // DATABASE ACCESS
    // =========================================================

    private final TeacherDAO teacherDAO = new TeacherDAO();


    // =========================================================
    // EDIT MODE
    // =========================================================

    /*
     * null  → Add Teacher mode
     *
     * object → Edit Teacher mode
     */
    private Teacher selectedTeacher;


    // =========================================================
    // PICTURE STATE
    // =========================================================

    /*
     * Holds the picture selected from the user's computer.
     *
     * It is NOT immediately copied into application storage.
     *
     * It remains here temporarily until saveTeacher()
     * determines whether we are adding or updating.
     */
    private File selectedPictureFile;


    /*
     * Used mainly during EDIT mode.
     *
     * false → keep the existing picture unless a new one is selected
     *
     * true  → user explicitly removed the existing picture
     */
    private boolean pictureRemoved;


    // =========================================================
    // INITIALIZE
    // =========================================================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // =========================================================
        // GENDER COMBO BOX
        // =========================================================

        genderComboBox.getItems().addAll(
                "Male",
                "Female"
        );


        // =========================================================
        // BUTTON ACTIONS
        // =========================================================

        saveTeacherButton.setOnAction(event -> saveTeacher());

        cancelButton.setOnAction(event -> closeWindow());

        choosePictureButton.setOnAction(event -> choosePicture());

        removePictureButton.setOnAction(event -> removePicture());


        // =========================================================
        // ENTER KEY → MOVE FOCUS TO NEXT FIELD
        // =========================================================

        fullNameField.setOnAction(event -> genderComboBox.requestFocus());

        genderComboBox.setOnAction(event -> phoneField.requestFocus());

        phoneField.setOnAction(event -> emailField.requestFocus());

        emailField.setOnAction(event -> specializationField.requestFocus());

        specializationField.setOnAction(event -> salaryField.requestFocus());

        salaryField.setOnAction(event -> saveTeacher());
    }


    private void saveTeacher() {

        // =========================================================
        // VALIDATION
        // =========================================================

        if (fullNameField.getText().trim().isEmpty()) {

            showError("Full name is required.");

            fullNameField.requestFocus();

            return;
        }


        if (genderComboBox.getValue() == null) {

            showError("Please select a gender.");

            genderComboBox.requestFocus();

            return;
        }


        if (phoneField.getText().trim().isEmpty()) {

            showError("Phone number is required.");

            phoneField.requestFocus();

            return;
        }


        String phone = phoneField.getText().trim();


        if (!phone.matches("\\d{10}")) {

            showError("Phone number must contain exactly 10 digits.");

            phoneField.requestFocus();

            return;
        }


        if (emailField.getText().trim().isEmpty()) {

            showError("Email address is required.");

            emailField.requestFocus();

            return;
        }


        if (specializationField.getText().trim().isEmpty()) {

            showError("Specialization is required.");

            specializationField.requestFocus();

            return;
        }


        if (salaryField.getText().trim().isEmpty()) {

            showError("Salary is required.");

            salaryField.requestFocus();

            return;
        }


        // =========================================================
        // VALIDATE SALARY
        // =========================================================

        double salary;

        try {

            salary = Double.parseDouble(salaryField.getText().trim());

        } catch (NumberFormatException e) {

            showError("Salary must be a valid number.");

            salaryField.requestFocus();

            return;
        }


        if (salary < 0) {

            showError("Salary cannot be negative.");

            salaryField.requestFocus();

            return;
        }


        // =========================================================
        // CREATE TEACHER MODEL
        // =========================================================

        Teacher teacher = new Teacher();

        teacher.setFullName(fullNameField.getText().trim());

        teacher.setGender(genderComboBox.getValue());

        teacher.setPhone(phoneField.getText().trim());

        teacher.setEmail(emailField.getText().trim());

        teacher.setSpecialization(specializationField.getText().trim());

        teacher.setSalary(salary);

        // New teachers start as Active.
        teacher.setStatus("Active");


        // =========================================================
        // ADD NEW TEACHER
        // =========================================================

        if (selectedTeacher == null) {

            /*
             * Picture path is initially NULL.
             *
             * We cannot create teacher25.jpg yet because
             * we don't know the teacher ID.
             */
            teacher.setPicturePath(null);


            /*
             * Save the teacher first.
             *
             * SQLite generates the teacher_id.
             *
             * Example:
             *
             * teacher_id = 25
             */
            int generatedTeacherId = teacherDAO.addTeacher(teacher);


            // -1 means insertion failed.
            if (generatedTeacherId == -1) {

                showError("Unable to save teacher.");

                return;
            }


            /*
             * Now we finally know the generated ID.
             *
             * If a picture was selected:
             *
             * teacher25.jpg
             *
             * will be created.
             */
            if (selectedPictureFile != null) {

                String picturePath = saveTeacherPicture(generatedTeacherId);


                /*
                 * Only update the database if the picture
                 * was successfully copied.
                 */
                if (picturePath != null) {

                    boolean pictureUpdated = teacherDAO.updateTeacherPicturePath(generatedTeacherId, picturePath);


                    if (!pictureUpdated) {

                        showError("Teacher was saved, but the picture path could not be saved.");

                        return;
                    }
                }
            }


            // =====================================================
            // SUCCESS — ADD MODE
            // =====================================================

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("Success");

            alert.setHeaderText(null);

            alert.setContentText("Teacher saved successfully.");

            alert.showAndWait();

            closeWindow();

            return;
        }


        // =========================================================
        // UPDATE EXISTING TEACHER
        // =========================================================

        /*
         * Keep the existing teacher ID.
         */
        teacher.setTeacherID(selectedTeacher.getTeacherID());


        // =========================================================
        // PICTURE HANDLING — UPDATE MODE
        // =========================================================

        if (pictureRemoved) {

            /*
             * User explicitly removed the picture.
             *
             * Store NULL in picture_path.
             */
            teacher.setPicturePath(null);

        }

        else if (selectedPictureFile != null) {

            /*
             * User selected a NEW picture.
             *
             * The picture will use the existing teacher ID.
             *
             * Example:
             *
             * teacher25.jpg
             */
            String picturePath = saveTeacherPicture(selectedTeacher.getTeacherID());


            if (picturePath == null) {

                showError("Unable to save the new teacher picture.");

                return;
            }


            teacher.setPicturePath(picturePath);

        }

        else {

            /*
             * User did not select a new picture and did not
             * remove the existing one.
             *
             * Therefore, KEEP the old filename.
             */
            teacher.setPicturePath(selectedTeacher.getPicturePath());
        }


        // =========================================================
        // UPDATE DATABASE
        // =========================================================

        boolean success = teacherDAO.updateTeacher(teacher);


        if (!success) {

            showError("Unable to update teacher.");

            return;
        }


        // =========================================================
        // SUCCESS — UPDATE MODE
        // =========================================================

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle("Success");

        alert.setHeaderText(null);

        alert.setContentText("Teacher updated successfully.");

        alert.showAndWait();

        closeWindow();
    }



    public void setTeacher(Teacher teacher) {

        // =========================================================
        // STORE THE SELECTED TEACHER
        // =========================================================

        selectedTeacher = teacher;


        // =========================================================
        // LOAD TEACHER INFORMATION INTO THE FORM
        // =========================================================

        fullNameField.setText(teacher.getFullName());

        genderComboBox.setValue(teacher.getGender());

        phoneField.setText(teacher.getPhone());

        emailField.setText(teacher.getEmail());

        specializationField.setText(teacher.getSpecialization());

        salaryField.setText(String.valueOf(teacher.getSalary()));


        // =========================================================
        // EXISTING PROFILE PICTURE
        // =========================================================

        if (teacher.getPicturePath() != null && !teacher.getPicturePath().isBlank()) {

            File pictureFile = new File(AppDataManager.getTeacherPicturesFolder(), teacher.getPicturePath());

            if (pictureFile.exists()) {

                Image image = new Image(pictureFile.toURI().toString());

                profileImageView.setImage(image);

                picturePlaceholder.setVisible(false);
                picturePlaceholder.setManaged(false);

                pictureFileNameLabel.setText(teacher.getPicturePath());

            } else {

                // Picture path exists in database,
                // but the actual file cannot be found.

                profileImageView.setImage(null);

                picturePlaceholder.setVisible(true);
                picturePlaceholder.setManaged(true);

                pictureFileNameLabel.setText("Picture file not found");
            }

        } else {

            // Teacher has no profile picture.

            profileImageView.setImage(null);

            picturePlaceholder.setVisible(true);
            picturePlaceholder.setManaged(true);

            pictureFileNameLabel.setText("No picture selected");
        }


        // =========================================================
        // RESET PICTURE STATE
        // =========================================================

        /*
         * The picture currently displayed belongs to the existing
         * teacher. It is NOT a newly selected file.
         */
        selectedPictureFile = null;

        /*
         * The user has not clicked "Remove Picture" yet.
         */
        pictureRemoved = false;


        // =========================================================
        // CHANGE SAVE BUTTON TO UPDATE MODE
        // =========================================================

        saveTeacherButton.setText("Update Teacher");
    }



    private void choosePicture() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose Teacher Profile Picture");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Use the last folder if it still exists
        File lastFolder = AppDataManager.getLastTeacherPictureBrowseFolder();

        if (lastFolder != null) {

            fileChooser.setInitialDirectory(lastFolder);
        }

        Stage stage = (Stage) choosePictureButton.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        // User cancelled the file chooser
        if (file == null) {

            return;
        }

        // Remember the folder for next time
        AppDataManager.setLastTeacherPictureBrowseFolder(file.getParentFile());

        selectedPictureFile = file;

        // Display the selected picture immediately
        Image image = new Image(file.toURI().toString());

        profileImageView.setImage(image);

        // Hide the placeholder
        picturePlaceholder.setVisible(false);
        picturePlaceholder.setManaged(false);

        // Display the original filename
        pictureFileNameLabel.setText(file.getName());
    }


    private void removePicture() {

        // Forget the currently selected picture.
        selectedPictureFile = null;

        // Remove the picture from the preview.
        profileImageView.setImage(null);

        // Show the placeholder again.
        picturePlaceholder.setVisible(true);
        picturePlaceholder.setManaged(true);

        // Reset the filename label.
        pictureFileNameLabel.setText("No picture selected");

        /*
         * Important for EDIT mode:
         *
         * This tells saveTeacher() that the user deliberately
         * removed the existing picture.
         */
        pictureRemoved = true;
    }

    /*
     * THIS METHOD SAVES THE SELECTED PICTURE TO OUR CREATED FOLDER
     */

    private String saveTeacherPicture(int teacherId) {

        // Picture is optional.
        if (selectedPictureFile == null) {

            return null;
        }


        // Get our centralized TeacherPictures folder.
        File pictureFolder = AppDataManager.getTeacherPicturesFolder();


        // Create our clean ID-based filename.
        String fileName = createTeacherPictureFileName(teacherId, selectedPictureFile.getName());


        // Build the final destination path.
        Path destination = pictureFolder.toPath().resolve(fileName);


        try {

            /*
             * Copy the user's selected picture into
             * our application's TeacherPictures folder.
             *
             * REPLACE_EXISTING is useful when editing a teacher
             * and replacing an existing picture.
             */
            Files.copy(selectedPictureFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    private String createTeacherPictureFileName(int teacherId, String originalFileName) {

        String extension = "";

        int dotIndex = originalFileName.lastIndexOf('.');

        if (dotIndex >= 0) {

            extension = originalFileName.substring(dotIndex);
        }

        return "teacher" + teacherId + extension;
    }

    private File createTeacherPicturesFolder() {

        return AppDataManager.getTeacherPicturesFolder();

    }


    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle("Validation Error");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    private void closeWindow() {

        Stage stage =
                (Stage) cancelButton
                        .getScene()
                        .getWindow();

        stage.close();
    }


}