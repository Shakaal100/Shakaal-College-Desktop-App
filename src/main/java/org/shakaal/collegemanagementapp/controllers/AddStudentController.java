package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.shakaal.collegemanagementapp.dao.CourseDAO;
import org.shakaal.collegemanagementapp.dao.StudentDAO;
import org.shakaal.collegemanagementapp.models.Course;
import org.shakaal.collegemanagementapp.models.Student;


public class AddStudentController implements Initializable{

    // CONTROLS BROOOOOH  !!!

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private DatePicker dateOfBirthPicker;

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

    //Constructors


    CourseDAO courseDAO = new CourseDAO();
    StudentDAO studentDAO = new StudentDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        genderComboBox.getItems().addAll("Male", "Female");

        courseComboBox.setItems(courseDAO.getAllCourses());

        saveButton.setOnAction(event->  saveStudent());

        cancelButton.setOnAction(event -> closeWindow());

        //Forward The focus to next Field whenever enter is pressed

        firstNameField.setOnAction(event -> lastNameField.requestFocus());

        lastNameField.setOnAction(event -> genderComboBox.requestFocus());

        phoneField.setOnAction(event -> emailField.requestFocus());

        emailField.setOnAction(event -> addressArea.requestFocus());

        genderComboBox.setOnAction(event -> dateOfBirthPicker.requestFocus());
    }

    public void setStudent(Student student) {

        selectedStudent = student;

        firstNameField.setText(student.getFirstName());

        lastNameField.setText(student.getLastName());

        genderComboBox.setValue(student.getGender());

        dateOfBirthPicker.setValue(student.getDateOfBirth());

        phoneField.setText(student.getPhone());

        emailField.setText(student.getEmail());

        addressArea.setText(student.getAddress());

        for (Course course : courseComboBox.getItems()) {

            if (course.getCourseId() == student.getCourseId()) {

                courseComboBox.setValue(course);

                break;
            }
        }

        saveButton.setText("Update Student");
    }

    private void saveStudent() {

        Student student = new Student();

        student.setFirstName(firstNameField.getText());

        student.setLastName(lastNameField.getText());

        student.setGender(genderComboBox.getValue());

        student.setDateOfBirth(dateOfBirthPicker.getValue());

        student.setPhone(phoneField.getText());

        student.setEmail(emailField.getText());

        student.setAddress(addressArea.getText());

        Course selectedCourse = courseComboBox.getValue();

        student.setCourseId(selectedCourse.getCourseId());

        StudentDAO studentDAO = new StudentDAO();

        boolean success;

        if (selectedStudent != null) {

            student.setStudentId(selectedStudent.getStudentId());

            success = studentDAO.updateStudent(student);

        } else {

            success = studentDAO.addStudent(student);
        }

        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Success");

            alert.setHeaderText(null);

            if (selectedStudent != null) {

                alert.setContentText("Student updated successfully.");

            } else {

                alert.setContentText("Student saved successfully.");
            }

            alert.showAndWait();

            closeWindow();
        }
    }

    private void closeWindow() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();
    }


}
