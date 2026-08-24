package org.shakaal.collegemanagementapp.controllers;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.TeacherDAO;
import org.shakaal.collegemanagementapp.models.Teacher;
import org.shakaal.collegemanagementapp.session.Session;

import java.io.IOException;
import java.util.Optional;

public class TeacherController {

    @FXML
    private TableView<Teacher> teacherTable;

    @FXML
    private TableColumn<Teacher, Integer> teacherIdColumn;

    @FXML
    private TableColumn<Teacher, String> fullNameColumn;

    @FXML
    private TableColumn<Teacher, String> genderColumn;

    @FXML
    private TableColumn<Teacher, String> phoneColumn;

    @FXML
    private TableColumn<Teacher, String> specializationColumn;

    @FXML
    private TableColumn<Teacher, Double> salaryColumn;

    @FXML
    private TableColumn<Teacher, String> statusColumn;

    @FXML
    private TableColumn<Teacher, Void> actionsColumn;


    @FXML
    private Label totalTeachersCountLabel;

    @FXML
    private Label maleTeachersCountLabel;

    @FXML
    private Label femaleTeachersCountLabel;

    @FXML
    private Label activeTeachersCountLabel;

    @FXML
    private Label malePercentageLabel;

    @FXML
    private Label femalePercentageLabel;

    @FXML
    private Label activePercentageLabel;


    @FXML
    private ComboBox<String> genderFilterComboBox;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Button addTeacherButton;


    private final TeacherDAO teacherDAO = new TeacherDAO();

    private ObservableList<Teacher> teacherList;


    @FXML
    private void initialize() {

        configureTable();

        loadGenderFilter();

        loadStatusFilter();

        loadTeachers();

        // loadStatistics();

        configureFilters();

        configureStatusColumn();

        configureActionColumn();

        loadStatistics();


        Image image = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/search.png"));

        searchIcon.setImage(image);

    }

    private void loadTeachers() {

        teacherList = teacherDAO.getAllTeachers();

        teacherTable.setItems(teacherList);

    }

    private void configureTable() {

        teacherIdColumn.setCellValueFactory(new PropertyValueFactory<>("teacherID"));

        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        // ***************** SALARY FORMATTING *****************

        salaryColumn.setCellFactory(column -> new TableCell<Teacher, Double>() {

            @Override
            protected void updateItem(Double salary, boolean empty) {

                super.updateItem(salary, empty);

                if (empty || salary == null) {

                    setText(null);

                } else {

                    setText(String.format("$%.2f", salary));

                }

            }

        });


        // ***************** RESPONSIVE TABLE COLUMNS *****************

        teacherTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        teacherIdColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.06));

        fullNameColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.15));

        genderColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.09));

        phoneColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.12));

        specializationColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.16));

        salaryColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.11));

        statusColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.11));

        actionsColumn.prefWidthProperty().bind(teacherTable.widthProperty().multiply(0.18));


        // ***************** ALIGNMENT *****************

        teacherIdColumn.setStyle("-fx-alignment: CENTER;");

        genderColumn.setStyle("-fx-alignment: CENTER;");

        salaryColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setStyle("-fx-alignment: CENTER;");

        actionsColumn.setStyle("-fx-alignment: CENTER;");

    }

    private void loadStatistics() {

        int totalTeachers = teacherDAO.getTotalTeacherCount();

        int maleTeachers = teacherDAO.getMaleTeacherCount();

        int femaleTeachers = teacherDAO.getFemaleTeacherCount();

        int activeTeachers = teacherDAO.getActiveTeacherCount();


        // Display the numbers

        totalTeachersCountLabel.setText(String.valueOf(totalTeachers));

        maleTeachersCountLabel.setText(String.valueOf(maleTeachers));

        femaleTeachersCountLabel.setText(String.valueOf(femaleTeachers));

        activeTeachersCountLabel.setText(String.valueOf(activeTeachers));


        updatePercentages(totalTeachers, maleTeachers, femaleTeachers, activeTeachers);

    }

    private void updatePercentages(int total, int male, int female, int active) {

        if (total == 0) {

            malePercentageLabel.setText("0% of total teachers");

            femalePercentageLabel.setText("0% of total teachers");

            activePercentageLabel.setText("0% of total teachers");

            return;
        }


        double malePercentage = male * 100.0 / total;

        double femalePercentage = female * 100.0 / total;

        double activePercentage = active * 100.0 / total;


        malePercentageLabel.setText(String.format("%.1f%% of total teachers", malePercentage));

        femalePercentageLabel.setText(String.format("%.1f%% of total teachers", femalePercentage));

        activePercentageLabel.setText(String.format("%.1f%% of total teachers", activePercentage));

    }


    private void loadGenderFilter() {

        genderFilterComboBox.getItems().addAll("All Genders", "Male", "Female");

        genderFilterComboBox.getSelectionModel().selectFirst();

    }

    private void loadStatusFilter() {

        statusFilterComboBox.getItems().addAll("All Status", "Active", "Inactive");

        statusFilterComboBox.getSelectionModel().selectFirst();

    }

    private void filterTeachers() {

        teacherTable.setItems(teacherDAO.searchTeachers(searchField.getText(), genderFilterComboBox.getValue(), statusFilterComboBox.getValue()));

    }

    private void configureFilters() {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            filterTeachers();

        });

        genderFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            filterTeachers();

        });

        statusFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            filterTeachers();

        });

    }

    private void configureStatusColumn() {

        statusColumn.setCellFactory(column ->
                new TableCell<Teacher, String>() {

                    @Override
                    protected void updateItem(String status, boolean empty) {

                        super.updateItem(status, empty);

                        if (empty || status == null) {

                            setText(null);
                            setGraphic(null);

                            return;
                        }

                        Label badge = new Label(status);

                        badge.setCursor(Cursor.HAND);

                        badge.getStyleClass().add("status-badge");


                        // ---------- Badge Style ----------

                        if (status.equalsIgnoreCase("Active")) {

                            badge.getStyleClass().add("status-active");

                        } else {

                            badge.getStyleClass().add("status-inactive");

                        }


                        // ---------- Status Click ----------

                        badge.setOnMouseClicked(event -> {

                            Teacher teacher = getTableView().getItems().get(getIndex());


                            String newStatus = teacher.getStatus().equalsIgnoreCase("Active") ? "Inactive" : "Active";


                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                            alert.getDialogPane().getStyleClass().add("alert-dialog");

                            alert.setTitle("Confirm Status Change");

                            alert.setHeaderText(null);

                            alert.setContentText(
                                    "Are you sure you want to change the status of\n\n"
                                            + teacher.getFullName()
                                            + "\n\nto "
                                            + newStatus
                                            + "?"
                            );


                            Optional<ButtonType> result = alert.showAndWait();


                            if (result.isPresent() && result.get() == ButtonType.OK) {


                                boolean success = teacherDAO.updateTeacherStatus(teacher.getTeacherID(), newStatus);


                                if (success) {

                                    refreshTeachers();

                                    loadStatistics();

                                } else {

                                    Alert error = new Alert(Alert.AlertType.ERROR);

                                    alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                                    alert.getDialogPane().getStyleClass().add("alert-dialog");

                                    error.setTitle("Failed");

                                    error.setHeaderText(null);

                                    error.setContentText("Failed to update teacher status.");

                                    error.showAndWait();

                                }

                            }

                        });


                        setGraphic(badge);

                        setText(null);

                    }

                });

    }

    private void configureActionColumn() {

        actionsColumn.setCellFactory(param ->
                new TableCell<>() {

                    private final Button editButton = new Button();

                    private final Button infoButton = new Button();

                    private final Button deleteButton = new Button();

                    private final HBox buttons = new HBox(10);

                    {

                        buttons.getChildren().addAll(editButton, infoButton);

                        if (Session.getCurrentUser().getRole().equals("ADMIN")) {

                            buttons.getChildren().add(deleteButton);

                        }

                    }

                    {

                        editButton.getStyleClass().add("edit-button");

                        infoButton.getStyleClass().add("info-button");

                        deleteButton.getStyleClass().add("delete-button");


                        // ---------- Edit Icon ----------

                        Image editImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/edit.png"));

                        ImageView editView = new ImageView(editImage);

                        editView.setFitWidth(16);

                        editView.setFitHeight(16);

                        editButton.setGraphic(editView);


                        // ---------- Info Icon ----------

                        Image infoImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/info.png"));

                        ImageView infoView = new ImageView(infoImage);

                        infoView.setFitWidth(18);

                        infoView.setFitHeight(18);

                        infoButton.setGraphic(infoView);


                        // ---------- Delete Icon ----------

                        Image deleteImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/delete.png"));

                        ImageView deleteView = new ImageView(deleteImage);

                        deleteView.setFitWidth(20);

                        deleteView.setFitHeight(20);

                        deleteButton.setGraphic(deleteView);


                        buttons.setAlignment(Pos.CENTER);


                        // ---------- Edit ----------

                        editButton.setOnAction(event -> {

                            Teacher teacher = getTableView().getItems().get(getIndex());

                            openEditTeacherWindow(teacher);

                        });


                        // ---------- Info ----------

                        infoButton.setOnAction(event -> {

                            Teacher teacher = getTableView().getItems().get(getIndex());

                            System.out.println("=================================");
                            System.out.println("INFO BUTTON CLICKED");
                            System.out.println("Teacher ID: " + teacher.getTeacherID());
                            System.out.println("Teacher Name: " + teacher.getFullName());
                            System.out.println("Picture Path: [" + teacher.getPicturePath() + "]");
                            System.out.println("Teacher Object: " + teacher);
                            System.out.println("=================================");

                            openTeacherInfo(teacher);

                        });


                        // ---------- Delete ----------

                        deleteButton.setOnAction(event -> {

                            Teacher teacher = getTableView().getItems().get(getIndex());

                            deleteTeacher(teacher);

                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {

                        super.updateItem(item, empty);

                        if (empty) {

                            setGraphic(null);

                        } else {

                            setGraphic(buttons);

                        }

                    }

                });

    }

    @FXML
    private void handleAddTeacher() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-teacher.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Teacher");

            Scene scene = new Scene(root, 900, 650);

            stage.setScene(scene);

            stage.setResizable(false);

            stage.showAndWait();
            refreshTeachers();
            loadTeachers();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void openEditTeacherWindow(
            Teacher teacher
    ) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-teacher.fxml"));


            Parent root = loader.load();


            AddTeacherController controller = loader.getController();


            // Pass the selected teacher to the AddTeacherController.
            // This switches it into UPDATE mode.
            controller.setTeacher(teacher);


            Stage stage = new Stage();


            stage.setTitle("Edit Teacher");


            Scene scene = new Scene(root, 900, 650);


            stage.setScene(scene);

            stage.setResizable(false);

            stage.showAndWait();

            refreshTeachers();

            loadStatistics();


        } catch (IOException e) {

            e.printStackTrace();
        }
    }



    private void openTeacherInfo(Teacher teacher) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/teacher-info-window.fxml"));

            Parent root = loader.load();

            TeacherInfoController controller = loader.getController();

            // Pass the selected teacher to the info window
            controller.setTeacher(teacher);

            Stage stage = new Stage();

            stage.setTitle("Teacher Information");

            Scene scene = new Scene(root, 850, 650);

            stage.setScene(scene);

            stage.setResizable(false);

            stage.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private void deleteTeacher(Teacher teacher) {

        // ---------- ADMIN CHECK ----------

        if (!Session.getCurrentUser().getRole().equalsIgnoreCase("ADMIN")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("Access Denied");

            alert.setHeaderText(null);

            alert.setContentText("Only administrators can delete teachers.");

            alert.showAndWait();

            return;
        }


        // ---------- CONFIRMATION ----------

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle("Delete Teacher");

        alert.setHeaderText(null);

        alert.setContentText("Are you sure you want to delete\n\n" + teacher.getFullName() + "?");

        Optional<ButtonType> result = alert.showAndWait();


        // ---------- DELETE ----------

        if (result.isPresent() && result.get() == ButtonType.OK) {

            boolean success = teacherDAO.deleteTeacher(teacher.getTeacherID());


            // ---------- SUCCESS ----------

            if (success) {

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);

                alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                alert.getDialogPane().getStyleClass().add("alert-dialog");

                successAlert.setTitle("Success");

                successAlert.setHeaderText(null);

                successAlert.setContentText("Teacher \n\n\"" + teacher.getFullName() + "\"\n\n was deleted successfully.");

                successAlert.showAndWait();


                // Refresh table + statistics

                refreshTeachers();


            } else {

                // ---------- FAILURE ----------

                Alert error = new Alert(Alert.AlertType.ERROR);

                alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                alert.getDialogPane().getStyleClass().add("alert-dialog");

                error.setTitle("Failed");

                error.setHeaderText(null);

                error.setContentText("Failed to delete teacher.");

                error.showAndWait();

            }
        }
    }

    private void refreshTeachers() {

        loadStatistics();

        filterTeachers();

    }
}
