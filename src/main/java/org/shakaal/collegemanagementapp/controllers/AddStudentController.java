package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.shakaal.collegemanagementapp.dao.CourseDAO;
import org.shakaal.collegemanagementapp.dao.StudentDAO;
import org.shakaal.collegemanagementapp.models.Course;
import org.shakaal.collegemanagementapp.models.Student;
import org.shakaal.collegemanagementapp.storage.AppDataManager;


public class AddStudentController implements Initializable{

    // CONTROLS   !!!

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextArea addressArea;

    @FXML
    private ComboBox<Course> courseComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Student selectedStudent;

    // Image Fields

    @FXML
    private ImageView profileImageView;

    @FXML
    private StackPane imagePreviewContainer;

    @FXML
    private VBox picturePlaceholder;

    @FXML
    private Button choosePictureButton;

    @FXML
    private Button removePictureButton;

    @FXML
    private Label pictureFileNameLabel;

    @FXML
    private ImageView collegeLogo;

    private File selectedPictureFile;

    private boolean pictureRemoved = false;

    //Constructors


    CourseDAO courseDAO = new CourseDAO();
    StudentDAO studentDAO = new StudentDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        genderComboBox.getItems().addAll("Male", "Female");

        courseComboBox.setItems(courseDAO.getAllCourses());

        saveButton.setOnAction(event->  saveStudent());

        cancelButton.setOnAction(event -> closeWindow());

        choosePictureButton.setOnAction(event -> choosePicture());

        removePictureButton.setOnAction(event -> removePicture());

        //Forward The focus to next Field whenever enter is pressed

        firstNameField.setOnAction(event -> lastNameField.requestFocus());

        lastNameField.setOnAction(event -> genderComboBox.requestFocus());

        phoneField.setOnAction(event -> emailField.requestFocus());

        emailField.setOnAction(event -> addressArea.requestFocus());

        //genderComboBox.setOnAction(event -> dateOfBirthPicker.requestFocus());
    }

    public void setStudent(Student student) {

        selectedStudent = student;

        firstNameField.setText(student.getFirstName());

        lastNameField.setText(student.getLastName());

        genderComboBox.setValue(student.getGender());

        phoneField.setText(student.getPhone());

        emailField.setText(student.getEmail());

        addressArea.setText(student.getAddress());


        // Select the student's existing course

        for (Course course : courseComboBox.getItems()) {

            if (course.getCourseId() == student.getCourseId()) {

                courseComboBox.setValue(course);

                break;
            }
        }



        // EXISTING PROFILE PICTURE

        if (student.getPicturePath() != null && !student.getPicturePath().isBlank()) {

            File pictureFile = new File(AppDataManager.getStudentPicturesFolder(), student.getPicturePath());

            if (pictureFile.exists()) {

                Image image = new Image(pictureFile.toURI().toString());

                profileImageView.setImage(image);

                picturePlaceholder.setVisible(false);
                picturePlaceholder.setManaged(false);

                pictureFileNameLabel.setText(student.getPicturePath());

            } else {

                profileImageView.setImage(null);

                picturePlaceholder.setVisible(true);
                picturePlaceholder.setManaged(true);

                pictureFileNameLabel.setText("Picture file not found");
            }

        } else {

            profileImageView.setImage(null);

            picturePlaceholder.setVisible(true);
            picturePlaceholder.setManaged(true);

            pictureFileNameLabel.setText("No picture selected");
        }


        // Reset picture state

        selectedPictureFile = null;

        pictureRemoved = false;


        saveButton.setText("Update Student");
    }





    private void saveStudent() {

        // =========================================================
        // 1. VALIDATE REQUIRED FIELDS
        // =========================================================

        if (firstNameField.getText().trim().isEmpty()) {

            showError("First name is required.");

            firstNameField.requestFocus();

            return;
        }

        if (lastNameField.getText().trim().isEmpty()) {

            showError("Last name is required.");

            lastNameField.requestFocus();

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


        // =========================================================
        // 2. VALIDATE PHONE NUMBER
        // =========================================================

        String phone = phoneField.getText().trim();

        if (!phone.matches("\\d{10}")) {

            showError("Phone number must contain exactly 10 digits.");

            phoneField.requestFocus();

            return;
        }


        // =========================================================
        // 3. VALIDATE EMAIL
        // =========================================================

        if (emailField.getText().trim().isEmpty()) {

            showError("Email address is required.");

            emailField.requestFocus();

            return;
        }


        // =========================================================
        // 4. VALIDATE ADDRESS
        // =========================================================

        if (addressArea.getText().trim().isEmpty()) {

            showError("Address is required.");

            addressArea.requestFocus();

            return;
        }


        // =========================================================
        // 5. VALIDATE COURSE
        // =========================================================

        if (courseComboBox.getValue() == null) {

            showError("Please select a course.");

            courseComboBox.requestFocus();

            return;
        }


        // =========================================================
        // 6. GET SELECTED COURSE
        // =========================================================

        Course selectedCourse = courseComboBox.getValue();


        // =========================================================
        // 7. CREATE STUDENT OBJECT
        // =========================================================

        Student student = new Student();

        student.setFirstName(firstNameField.getText().trim());

        student.setLastName(lastNameField.getText().trim());

        student.setGender(genderComboBox.getValue());

        student.setPhone(phone);

        student.setEmail(emailField.getText().trim());

        student.setAddress(addressArea.getText().trim());

        student.setCourseId(selectedCourse.getCourseId());

        student.setStatus("Active");


        // =========================================================
        // 8. HANDLE ADD / UPDATE DIFFERENTLY
        // =========================================================

        boolean success;


        // =========================================================
        // ADDING A NEW STUDENT
        // =========================================================

        if (selectedStudent == null) {

            /*
             * A new student does not have an ID yet.
             *
             * Therefore, we cannot create:
             *
             * student25.jpg
             *
             * until SQLite gives us the generated student ID.
             *
             * So we initially save the student without a picture path.
             */

            student.setRegisteredDate(LocalDate.now());

            student.setPicturePath(null);



            // INSERT STUDENT AND GET GENERATED Int

            int generatedStudentId = studentDAO.addStudent(student);


            // -1 means the database insertion failed

            if (generatedStudentId == -1) {

                showError("Failed to save student.");

                return;
            }

            // PICTURE IS OPTIONAL


            if (selectedPictureFile != null) {

                /*
                 * Now we finally know the student's ID.
                 *
                 * Example:
                 *
                 * generatedStudentId = 25
                 *
                 * Picture becomes:
                 *
                 * student25.jpg
                 */

                String picturePath = saveStudentPicture(generatedStudentId);


                // CHECK WHETHER PICTURE WAS COPIED SUCCESSFULLY

                if (picturePath == null) {

                    showError("Student was saved, but the profile picture could not be saved.");

                    return;
                }


                // STORE PICTURE PATH IN DATABASE

                boolean pictureUpdated = studentDAO.updateStudentPicturePath(generatedStudentId, picturePath);


                if (!pictureUpdated) {

                    showError("Student was saved, but the picture information could not be stored.");

                    return;
                }
            }


            success = true;

        }



        // UPDATING AN EXISTING STUDENT


        else {

            /*
             * The student already has an ID.
             *
             * We can therefore determine exactly which student's
             * picture belongs to them.
             */

            student.setStudentId(selectedStudent.getStudentId());


            // KEEP THE ORIGINAL REGISTERED DATE

            student.setRegisteredDate(selectedStudent.getRegisteredDate());


            // START WITH EXISTING PICTURE PATH

            String picturePath = selectedStudent.getPicturePath();


            // USER REMOVED THE PICTURE

            if (pictureRemoved) {

                /*
                 * The user deliberately clicked
                 * "Remove Picture".
                 *
                 * Therefore, the database should contain NULL.
                 */

                picturePath = null;
            }


            // -----------------------------------------------------
            // USER SELECTED A NEW PICTURE


            else if (selectedPictureFile != null) {

                /*
                 * The student already has an ID.
                 *
                 * Example:
                 *
                 * student25.jpg
                 *
                 * If the user chooses another picture, the new
                 * picture will also use the student's ID:
                 *
                 * student25.png
                 */

                picturePath = saveStudentPicture(selectedStudent.getStudentId());


                if (picturePath == null) {

                    showError("The new profile picture could not be saved.");

                    return;
                }
            }



            // PUT PICTURE PATH INTO STUDENT OBJECT
            // -----------------------------------------------------

            student.setPicturePath(picturePath);


            // -----------------------------------------------------
            // UPDATE STUDENT


            success = studentDAO.updateStudent(student);
        }

        // 9. SHOW SUCCESS MESSAGE

        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);


            // Use our global alert styling

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");


            alert.setTitle("Success");

            alert.setHeaderText(null);


            if (selectedStudent != null) {

                alert.setContentText("Student updated successfully.");

            } else {

                alert.setContentText("Student saved successfully.");
            }


            alert.showAndWait();


            // Close the Add/Edit Student window

            closeWindow();
        }
    }





    // =====================================================
// CHOOSE PROFILE PICTURE
// =====================================================

    private void choosePicture() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose Student Profile Picture");

        // Remember the last folder
        File lastFolder = AppDataManager.getLastStudentPictureBrowseFolder();

        if (lastFolder != null) {
            fileChooser.setInitialDirectory(lastFolder);
        }

        // Open file chooser
        Stage stage = (Stage) choosePictureButton.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        // Remember selected file's folder
        AppDataManager.setLastStudentPictureBrowseFolder(file.getParentFile());

        selectedPictureFile = file;

        Image image = new Image(file.toURI().toString());

        profileImageView.setImage(image);

        picturePlaceholder.setVisible(false);
        picturePlaceholder.setManaged(false);

        pictureFileNameLabel.setText(file.getName());
    }



    private void removePicture() {

        selectedPictureFile = null;

        profileImageView.setImage(null);

        picturePlaceholder.setVisible(true);

        picturePlaceholder.setManaged(true);

        pictureFileNameLabel.setText("No picture selected");
    }


    private String saveStudentPicture(int studentId) {

        if (selectedPictureFile == null) {

            return null;

        }

        File pictureFolder = AppDataManager.getStudentPicturesFolder();

        String fileName = createStudentPictureFileName(studentId, selectedPictureFile.getName());

        Path destination = pictureFolder.toPath().resolve(fileName);

        try {

            Files.copy(selectedPictureFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }



    private File createStudentPicturesFolder() {

        return AppDataManager.getStudentPicturesFolder();

    }

    //

    private String createStudentPictureFileName(int studentId, String originalFileName) {

        String extension = "";

        int dotIndex = originalFileName.lastIndexOf('.');

        if (dotIndex >= 0) {

            extension = originalFileName.substring(dotIndex);

        }

        return "student" + studentId + extension;
    }




    private void closeWindow() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();
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


}
