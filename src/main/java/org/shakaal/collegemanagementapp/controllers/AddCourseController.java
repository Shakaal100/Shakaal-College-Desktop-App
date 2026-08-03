package org.shakaal.collegemanagementapp.controllers;
import org.shakaal.collegemanagementapp.models.Course;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.CourseDAO;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AddCourseController implements Initializable {

    @FXML
    private TextField courseCodeField;

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseFeeField;

    @FXML
    private ComboBox<String> durationComboBox;

    @FXML
    private ComboBox<String> scheduleComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ImageView browseIcon;

    @FXML
    private Label browseLabel;

    @FXML
    private Label selectedFileLabel;

    private File selectedPdfFile;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Course selectedCourse;

    private boolean pdfChanged = false;

    private final CourseDAO courseDAO = new CourseDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        configureComboBoxes();

        configureBrowsePdf();

        configureButtons();

    }


    private void configureComboBoxes() {

        durationComboBox.getItems().addAll(DURATIONS);

        scheduleComboBox.getItems().addAll(SCHEDULES);

        statusComboBox.getItems().addAll(STATUSES);

        statusComboBox.setValue("Available");

    }

    private static final List<String> DURATIONS = List.of(
            "6 Months",
            "1 Year",
            "2 Years",
            "3 Years",
            "4 Years",
            "5 Years"
    );

    private static final List<String> SCHEDULES = List.of(
            "Morning",
            "Evening",
            "Night"
    );

    private static final List<String> STATUSES = List.of(
            "Available",
            "Archived"
    );

    private void configureBrowsePdf() {

        browseLabel.setOnMouseClicked(event -> browsePdf());

        browseIcon.setOnMouseClicked(event -> browsePdf());

    }


    private void browsePdf() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select Course Information PDF");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        Stage stage = (Stage) browseLabel.getScene().getWindow();

        selectedPdfFile = fileChooser.showOpenDialog(stage);

        if (selectedPdfFile != null) {

            selectedFileLabel.setText(selectedPdfFile.getName());

            pdfChanged = true;

        }

    }


    private void configureButtons() {

        saveButton.setOnAction(event -> saveCourse());

        cancelButton.setOnAction(event -> cancel());

    }


    private void cancel() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();

    }

    private void saveCourse() {

        if (courseCodeField.getText().trim().isEmpty() ||
                courseNameField.getText().trim().isEmpty() ||
                durationComboBox.getValue() == null ||
                scheduleComboBox.getValue() == null ||
                courseFeeField.getText().trim().isEmpty() ||
                statusComboBox.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Missing Information");

            alert.setHeaderText(null);

            alert.setContentText("Please fill in all required fields.");

            alert.showAndWait();

            return;

        }

        // *************** validate course code exists, only when adding a new course *************************

        if (selectedCourse == null) {

            if (courseDAO.courseCodeExists(courseCodeField.getText().trim())) {

                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Duplicate Course");

                alert.setHeaderText(null);

                alert.setContentText("Course code already exists.");

                alert.showAndWait();

                return;

            }

        } else {

            if (courseDAO.courseCodeExists(
                    courseCodeField.getText().trim(),
                    selectedCourse.getCourseId())) {

                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Duplicate Course");

                alert.setHeaderText(null);

                alert.setContentText("Course code already exists.");

                alert.showAndWait();

                return;

            }

        }

        // *************** Empty PDF validation *************************

        if (selectedPdfFile == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Missing PDF");

            alert.setHeaderText(null);

            alert.setContentText("Please select a course information PDF.");

            alert.showAndWait();

            return;

        }

        double courseFee;

        try {

            courseFee = Double.parseDouble(courseFeeField.getText().trim());

        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Invalid Fee");

            alert.setHeaderText(null);

            alert.setContentText("Please enter a valid course fee.");

            alert.showAndWait();

            return;

        }

        // *************** copying the file to The created coursePDFs folder *************************

        File pdfFolder = createCoursePdfFolder();

        Path destination = pdfFolder.toPath().resolve(selectedPdfFile.getName());

        try {

            Files.copy(
                    selectedPdfFile.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("PDF Error");

            alert.setHeaderText(null);

            alert.setContentText("Failed to copy course PDF.");

            alert.showAndWait();

            return;

        }

        Course course = new Course();

        course.setCourseCode(courseCodeField.getText().trim());

        course.setCourseName(courseNameField.getText().trim());

        course.setDuration(durationComboBox.getValue());

        course.setSchedule(scheduleComboBox.getValue());

        course.setCourseFee(courseFee);

        course.setStatus(statusComboBox.getValue());

        course.setCourseInfoPath(destination.toString());

        boolean success;

        if (selectedCourse == null) {

            success = courseDAO.addCourse(course);

        } else {

            course.setCourseId(selectedCourse.getCourseId());

            success = courseDAO.updateCourse(course);

        }

        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Success");

            alert.setHeaderText(null);

            alert.setContentText(
                    selectedCourse == null
                            ? "Course added successfully."
                            : "Course updated successfully."
            );

            alert.showAndWait();

            cancel();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");

            alert.setHeaderText(null);

            alert.setContentText(
                    selectedCourse == null
                            ? "Failed to add course."
                            : "Failed to update course."
            );

            alert.showAndWait();

        }

    }


    public void setCourse(Course course) {

        selectedCourse = course;

        courseCodeField.setText(course.getCourseCode());

        courseNameField.setText(course.getCourseName());

        durationComboBox.setValue(course.getDuration());

        scheduleComboBox.setValue(course.getSchedule());

        courseFeeField.setText(String.valueOf(course.getCourseFee()));

        statusComboBox.setValue(course.getStatus());

        if (course.getCourseInfoPath() != null &&
                !course.getCourseInfoPath().isBlank()) {

            selectedPdfFile = new File(course.getCourseInfoPath());

            selectedFileLabel.setText(selectedPdfFile.getName());

        }

        saveButton.setText("Update Course");

    }


    private File createCoursePdfFolder() {

        File pdfFolder = new File("CoursePDFs");

        if (!pdfFolder.exists()) {

            pdfFolder.mkdirs();

        }

        return pdfFolder;

    }
}
