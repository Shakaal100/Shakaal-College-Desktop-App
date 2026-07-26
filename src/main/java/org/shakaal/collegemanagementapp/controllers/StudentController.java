package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.shakaal.collegemanagementapp.dao.StudentDAO;
import org.shakaal.collegemanagementapp.models.Student;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class StudentController implements Initializable{


    //FXML CONTROLS

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Button addStudentButton;

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private TableView<Student> studentTable;

    //TABLE COLUMNS

    @FXML
    private TableColumn<Student, Integer> idColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> genderColumn;

    @FXML
    private TableColumn<Student, LocalDate> dobColumn;

    @FXML
    private TableColumn<Student, String> phoneColumn;

    @FXML
    private TableColumn<Student, Integer> courseColumn;

    @FXML
    private TableColumn<Student, String> statusColumn;

    @FXML
    private TableColumn<Student, Void> actionsColumn;

    private final StudentDAO studentDAO = new StudentDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        configureColumns();

        loadStudents();

        configureActionColumn();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            StudentDAO studentDAO = new StudentDAO();

            studentTable.setItems(studentDAO.searchStudents(newValue)
            );

        });

        addStudentButton.setOnAction(event -> openAddStudentWindow());

        studentTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // RESPONSIVE TABLE COLUMNS   **********


        idColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.06));

        firstNameColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.12));

        lastNameColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.12));

        genderColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.08));

        dobColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.12));

        phoneColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.12));

        courseColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.16));

        statusColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.08));

        actionsColumn.prefWidthProperty().bind(studentTable.widthProperty().multiply(0.11));

        idColumn.setStyle("-fx-alignment: CENTER;");

        genderColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setStyle("-fx-alignment: CENTER;");

        actionsColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setCellFactory(column -> new TableCell<Student, String>() {

            @Override
            protected void updateItem(String status, boolean empty) {

                super.updateItem(status, empty);

                if (empty || status == null) {

                    setText(null);
                    setGraphic(null);

                } else {

                    Label badge = new Label(status);

                    badge.setCursor(Cursor.HAND);

                    badge.getStyleClass().add("status-badge");

                    if (status.equalsIgnoreCase("Active")) {

                        badge.getStyleClass().add("status-active");

                    } else {

                        badge.getStyleClass().add("status-inactive");

                    }

                    badge.setOnMouseClicked(event -> {

                        Student student = getTableView().getItems().get(getIndex());

                        String newStatus = student.getStatus().equalsIgnoreCase("Active")
                                ? "Inactive"
                                : "Active";

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                        alert.setTitle("Confirm Status Change");
                        alert.setHeaderText(null);

                        alert.setContentText("Are you sure you want to change the status of\n\n"
                                        + student.getFirstName() + " "
                                        + student.getLastName()
                                        + "\n\nto "
                                        + newStatus + "?"
                        );

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {

                            boolean success = studentDAO.updateStudentStatus(
                                    student.getStudentId(),
                                    newStatus
                            );

                            if (success) {

                                refreshStudents();

                            } else {

                                Alert error = new Alert(Alert.AlertType.ERROR);

                                error.setTitle("Failed");

                                error.setHeaderText(null);

                                error.setContentText("Failed to update student status.");

                                error.showAndWait();
                            }
                        }

                    });

                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        Image image = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/search.png"));

        searchIcon.setImage(image);

    }

    private void configureColumns() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    private void loadStudents() {


            ObservableList<Student> students = studentDAO.getAllStudents();

            System.out.println("Students loaded = " + students.size());

            studentTable.setItems(students);

         totalStudentsLabel.setText("Total Students : " + students.size());


    }

    private void configureActionColumn() {

        actionsColumn.setCellFactory(param ->
                new TableCell<>() {

                    private final Button editButton = new Button();

                    private final Button deleteButton = new Button();

                    private final HBox buttons = new HBox(10, editButton, deleteButton);

                    {
                        editButton.getStyleClass().add("edit-button");
                        deleteButton.getStyleClass().add("delete-button");

                        //Image view ICONS for edit button

                        Image editImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/edit.png"));

                        ImageView editView = new ImageView(editImage);
                        editView.setFitWidth(16);
                        editView.setFitHeight(16);

                        editButton.setGraphic(editView);

                        //Image view ICONS for delete button

                        Image deleteImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/delete.png"));

                        ImageView deleteView = new ImageView(deleteImage);
                        deleteView.setFitWidth(20);
                        deleteView.setFitHeight(20);

                        deleteButton.setGraphic(deleteView);

                        buttons.setAlignment(Pos.CENTER);

                        editButton.setOnAction(event -> {

                            Student student = getTableView().getItems().get(getIndex());

                            openEditStudentWindow(student);

                        });

                        deleteButton.setOnAction(event -> {

                            Student student =
                                    getTableView().getItems().get(getIndex());

                            deleteStudent(student);

                        });
                    }

                    @Override
                    protected void updateItem(
                            Void item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttons);
                        }
                    }
                });
    }

    private void openAddStudentWindow() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/org/shakaal/collegemanagementapp/fxml/add-student.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Student");

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            refreshStudents();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void openEditStudentWindow(
            Student student
    ) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-student.fxml"));

            Parent root = loader.load();

            AddStudentController controller = loader.getController();

            controller.setStudent(student);

            Stage stage = new Stage();

            stage.setTitle("Edit Student");

            stage.setScene(new Scene(root));

            stage.showAndWait();

            refreshStudents();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void updateTotalStudents() {

        totalStudentsLabel.setText("Total Students: " + studentTable.getItems().size());
    }

    private void refreshStudents() {

        loadStudents();
    }

    private void deleteStudent(Student student) {

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Student"
        );

        confirmation.setHeaderText(
                null
        );

        confirmation.setContentText(
                "Are you sure you want to delete "
                        + student.getFirstName()
                        + " "
                        + student.getLastName()
                        + "?"
        );

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            StudentDAO studentDAO = new StudentDAO();

            boolean deleted = studentDAO.deleteStudent(student.getStudentId());

            if (deleted) {

                Alert success = new Alert(Alert.AlertType.INFORMATION);

                success.setTitle("Success");

                success.setHeaderText(null);

                success.setContentText("Student deleted successfully.");

                success.showAndWait();

                refreshStudents();
            }
        }
    }

}
