package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.CourseDAO;
import org.shakaal.collegemanagementapp.models.Course;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.shakaal.collegemanagementapp.session.Session;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class CourseController implements Initializable{

    @FXML
    private VBox legendContainer;

    @FXML
    private Label totalCoursesCountLabel;

    @FXML
    private Label availableCoursesCountLabel;

    @FXML
    private Label archivedCoursesCountLabel;

    @FXML
    private PieChart coursePieChart;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchIcon;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    @FXML
    private Button addCourseButton;


    @FXML
    private TableView<Course> courseTable;


    @FXML
    private TableColumn<Course, Integer> idColumn;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> scheduleColumn;

    @FXML
    private TableColumn<Course, Double> courseFeeColumn;

    @FXML
    private TableColumn<Course, Integer> studentsCountColumn;

    @FXML
    private TableColumn<Course, String> durationColumn;

    @FXML
    private TableColumn<Course, String> statusColumn;

    @FXML
    private TableColumn<Course, Void> actionsColumn;



    private final CourseDAO courseDAO = new CourseDAO();

    private ObservableList<Course> courseList;


    private static final Color[] DASHBOARD_COLORS = {

            Color.web("#3B82F6"), // Blue
            Color.web("#10B981"), // Emerald
            Color.web("#F59E0B"), // Amber
            Color.web("#EF4444"), // Red
            Color.web("#8B5CF6"), // Purple
            Color.web("#06B6D4"), // Cyan
            Color.web("#F97316"), // Orange
            Color.web("#84CC16"), // Lime
            Color.web("#EC4899"), // Pink
            Color.web("#6366F1")  // Indigo

    };


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        configureTable();

        configureFilters();

        configureStatusColumn();

        configureButtons();

        configureActionColumn();

        refreshDashboard();

        Image image = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/search.png"));

        searchIcon.setImage(image);

    }

    // *************************************************************
    // ***************** TABLE CONFIGURATION *****************
    //***************************************************************

    private void configureTable() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        courseFeeColumn.setCellValueFactory(new PropertyValueFactory<>("courseFee"));
        studentsCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        courseFeeColumn.setCellFactory(column -> new TableCell<Course, Double>() {

            @Override
            protected void updateItem(Double fee, boolean empty) {

                super.updateItem(fee, empty);

                if (empty || fee == null) {

                    setText(null);

                } else {

                    setText(String.format("$%.2f", fee));

                }

            }

        });


        // ***************** RESPONSIVE TABLE COLUMNS *****************

        courseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        idColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.06));

        courseCodeColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.10));

        courseNameColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.19));

        scheduleColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.11));

        courseFeeColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.12));

        studentsCountColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.10));

        statusColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.11));

        actionsColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.19));

        idColumn.setStyle("-fx-alignment: CENTER;");

        scheduleColumn.setStyle("-fx-alignment: CENTER;");

        courseFeeColumn.setStyle("-fx-alignment: CENTER;");

        studentsCountColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setStyle("-fx-alignment: CENTER;");

        actionsColumn.setStyle("-fx-alignment: CENTER;");

    }

    private void loadCourses() {

        courseList = courseDAO.getAllCourses();

        courseTable.setItems(courseList);

    }

    private void loadPieChart() {

        ObservableList<PieChart.Data> data = courseDAO.getCoursePieChartData();

        coursePieChart.setData(data);

        Platform.runLater(() -> {

            int i = 0;

            for (PieChart.Data slice : data) {

                slice.getNode().setStyle(
                        "-fx-pie-color: " + toHex(DASHBOARD_COLORS[i % DASHBOARD_COLORS.length]) + ";");
                i++;
            }

            buildLegend(data);

        });

    }



    private void buildLegend(ObservableList<PieChart.Data> data) {

        legendContainer.getChildren().clear();

        int i = 0;

        for (PieChart.Data item : data) {

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            Circle circle = new Circle(7);
            circle.setFill(DASHBOARD_COLORS[i % DASHBOARD_COLORS.length]);

            Label label = new Label(item.getName() + " (" + (int)item.getPieValue() + ")");

            label.getStyleClass().add("legend-label");

            row.getChildren().addAll(circle, label);

            legendContainer.getChildren().add(row);

            i++;

        }

    }


    private void configureFilters() {

        // Search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            filterCourses();

        });

        // Status filter setup
        statusFilterComboBox.getItems().addAll(
                "All",
                "Available",
                "Archived"
        );

        statusFilterComboBox.setValue("All");

        // Status listener
        statusFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            filterCourses();

        });

    }
    // Helper method for courses filtering

    private void filterCourses() {

        String keyword = searchField.getText().trim();

        String status = statusFilterComboBox.getValue();

        courseTable.setItems(courseDAO.filterCourses(keyword, status));

    }


    private void loadStatistics() {

        totalCoursesCountLabel.setText(String.valueOf(courseDAO.getTotalCourseCount()));

        availableCoursesCountLabel.setText(String.valueOf(courseDAO.getAvailableCourseCount()));

        archivedCoursesCountLabel.setText(String.valueOf(courseDAO.getArchivedCourseCount()));

    }


    private void configureButtons() {

        addCourseButton.setOnAction(event -> handleAddCourse());

    }


    @FXML
    private void handleAddCourse() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-course.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Course");

            stage.setScene(new Scene(root));

            stage.setResizable(false);

            stage.show();

            refreshDashboard();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void configureStatusColumn() {

        statusColumn.setCellFactory(column -> new TableCell<Course, String>() {

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

                    if (status.equalsIgnoreCase("Available")) {

                        badge.getStyleClass().add("status-active");

                    } else {

                        badge.getStyleClass().add("status-inactive");

                    }

                    badge.setOnMouseClicked(event -> {

                        Course course = getTableView().getItems().get(getIndex());

                        String newStatus = course.getStatus().equalsIgnoreCase("Available")
                                ? "Archived"
                                : "Available";

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                        alert.getDialogPane().getStyleClass().add("alert-dialog");

                        alert.setTitle("Confirm Status Change");
                        alert.setHeaderText(null);

                        String message;

                        if (newStatus.equalsIgnoreCase("Archived")) {

                            message = "Are you sure you want to archive the course\n\n"
                                    + course.getCourseName()
                                    + "?";

                        } else {

                            message = "Are you sure you want to make the course\n\n"
                                    + course.getCourseName()
                                    + "\navailable again?";

                        }

                        alert.setContentText(message);

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {

                            boolean success = courseDAO.updateCourseStatus(
                                    course.getCourseId(),
                                    newStatus
                            );

                            if (success) {

                                refreshDashboard();

                            } else {

                                Alert error = new Alert(Alert.AlertType.ERROR);

                                error.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                                error.getDialogPane().getStyleClass().add("alert-dialog");

                                error.setTitle("Failed");

                                error.setHeaderText(null);

                                error.setContentText("Failed to update course status.");

                                error.showAndWait();

                            }

                        }

                    });

                    setGraphic(badge);

                    setText(null);

                }

            }

        });

    }


    private void configureActionColumn() {

        actionsColumn.setCellFactory(param -> new TableCell<>() {

            private final Button editButton = new Button();

            private final Button infoButton = new Button();

            private final Button deleteButton = new Button();



            private final HBox buttons = new HBox(10);

            {

                buttons.getChildren().add(editButton);

                if (Session.getCurrentUser().getRole().equals("ADMIN")) {

                    buttons.getChildren().add(infoButton);

                    buttons.getChildren().add(deleteButton);

                } else {

                    buttons.getChildren().add(infoButton);

                }

                buttons.setAlignment(Pos.CENTER);

                editButton.getStyleClass().add("edit-button");

                infoButton.getStyleClass().add("info-button");

                deleteButton.getStyleClass().add("delete-button");

                // ---------- Edit Icon ----------

                Image editImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/edit.png"));

                ImageView editView = new ImageView(editImage);

                editView.setFitWidth(16);

                editView.setFitHeight(16);

                editButton.setGraphic(editView);

                editButton.setTooltip(new Tooltip("Edit Course"));

                // ---------- Info Icon ----------

                Image infoImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/pdf.png"));

                ImageView infoView = new ImageView(infoImage);

                infoView.setFitWidth(18);

                infoView.setFitHeight(18);

                infoButton.setGraphic(infoView);

                infoButton.setTooltip(new Tooltip("View Course Information"));

                // ---------- Delete Icon ----------

                Image deleteImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/delete.png"));

                ImageView deleteView = new ImageView(deleteImage);

                deleteView.setFitWidth(18);

                deleteView.setFitHeight(18);

                deleteButton.setGraphic(deleteView);

                deleteButton.setTooltip(new Tooltip("Delete Course"));

                // ---------- Actions ----------

                editButton.setOnAction(event -> {

                    Course course = getTableView().getItems().get(getIndex());

                    openEditCourseWindow(course);

                });

                infoButton.setOnAction(event -> {

                    Course course = getTableView().getItems().get(getIndex());

                    openCourseInfo(course);

                });

                deleteButton.setOnAction(event -> {

                    Course course = getTableView().getItems().get(getIndex());

                    deleteCourse(course);

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

    private void openCourseInfo(Course course) {

        if (course.getCourseInfoPath() == null ||
                course.getCourseInfoPath().isBlank()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("No Course Information");

            alert.setHeaderText(null);

            alert.setContentText("This course has no PDF attached.");

            alert.showAndWait();

            return;

        }

        File pdfFile = new File(course.getCourseInfoPath());

        if (!pdfFile.exists()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("File Not Found");

            alert.setHeaderText(null);

            alert.setContentText("The course information PDF could not be found.");

            alert.showAndWait();

            return;

        }

        try {

            Desktop.getDesktop().open(pdfFile);

        } catch (IOException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("Unable to Open PDF");

            alert.setHeaderText(null);

            alert.setContentText("An error occurred while opening the PDF.");

            alert.showAndWait();

        }

    }


    private void openEditCourseWindow(Course course) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-course.fxml"));

            Parent root = loader.load();

            AddCourseController controller = loader.getController();

            controller.setCourse(course);

            Stage stage = new Stage();

            stage.setTitle("Edit Course");

            stage.setScene(new Scene(root));

            stage.setResizable(false);

            stage.showAndWait();

            refreshDashboard();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void deleteCourse(Course course) {

        if (course.getStudentsCount() > 0) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("alert-dialog");

            alert.setTitle("Cannot Delete");

            alert.setHeaderText(null);

            alert.setContentText("This course cannot be deleted because " + course.getStudentsCount() + " student(s) are currently enrolled.");

            alert.showAndWait();

            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle("Delete Course");

        alert.setHeaderText(null);

        alert.setContentText(
                "Are you sure you want to delete\n\n"
                        + course.getCourseName()
                        + " ?"
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            boolean success = courseDAO.deleteCourse(course.getCourseId());

            if (success) {

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);

                successAlert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                successAlert.getDialogPane().getStyleClass().add("alert-dialog");

                successAlert.setTitle("Success");

                successAlert.setHeaderText(null);

                successAlert.setContentText("Course deleted successfully.");

                successAlert.showAndWait();

                refreshDashboard();

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);

                errorAlert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

                errorAlert.getDialogPane().getStyleClass().add("alert-dialog");

                errorAlert.setTitle("Error");

                errorAlert.setHeaderText(null);

                errorAlert.setContentText("Failed to delete course.");

                errorAlert.showAndWait();

            }

        }

    }


    private void refreshDashboard() {

        loadCourses();

        loadStatistics();

        loadPieChart();

    }

    // ***************** COLOR HELPER METH0D **********

    private String toHex(Color color) {

        return String.format(
                "#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255)
        );

    }
}
