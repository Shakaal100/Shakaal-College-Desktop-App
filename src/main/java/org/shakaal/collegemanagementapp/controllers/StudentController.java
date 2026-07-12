package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.shakaal.collegemanagementapp.dao.StudentDAO;
import org.shakaal.collegemanagementapp.models.Student;

import java.net.URL;
import java.time.LocalDate;
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


public class StudentController implements Initializable{


    //FXML CONTROLS

    @FXML
    private TextField searchField;

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
    private TableColumn<Student, Void> actionsColumn;

    private final StudentDAO studentDAO = new StudentDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        configureColumns();

        loadStudents();

        configureActionColumn();

        addStudentButton.setOnAction(event -> openAddStudentWindow());
        /*
        studentTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);
        */
        System.out.println("BRO THE CONTROLLER IS RUNNING!");

    }

    private void configureColumns() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));


        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

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

                    private final Button editButton = new Button("Edit");

                    private final Button deleteButton = new Button("Delete");

                    private final HBox buttons = new HBox(30, editButton, deleteButton);

                    {
                        editButton.getStyleClass().add("edit-button");
                        deleteButton.getStyleClass().add("delete-button");

                        //Image view ICONS for edit button

                        Image editImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/edit.png"));

                        ImageView editView = new ImageView(editImage);
                        editView.setFitWidth(20);
                        editView.setFitHeight(20);

                        editButton.setGraphic(editView);

                        //Image view ICONS for delete button

                        Image deleteImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/delete.png"));

                        ImageView deleteView = new ImageView(deleteImage);
                        deleteView.setFitWidth(20);
                        deleteView.setFitHeight(20);

                        deleteButton.setGraphic(deleteView);

                        buttons.setAlignment(Pos.CENTER);

                        editButton.setOnAction(event -> {

                            Student student =
                                    getTableView().getItems().get(getIndex());

                            System.out.println(
                                    "Edit: " +
                                            student.getFirstName()
                            );

                        });

                        deleteButton.setOnAction(event -> {

                            Student student =
                                    getTableView().getItems().get(getIndex());

                            System.out.println(
                                    "Delete: " +
                                            student.getFirstName()
                            );

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

    private void updateTotalStudents() {

        totalStudentsLabel.setText(
                "Total Students: "
                        + studentTable.getItems().size()
        );
    }

    private void refreshStudents() {

        loadStudents();
    }


}
