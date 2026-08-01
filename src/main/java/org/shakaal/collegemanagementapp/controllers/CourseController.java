package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.shakaal.collegemanagementapp.dao.CourseDAO;
import org.shakaal.collegemanagementapp.models.Course;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

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

         loadCourses();

        loadPieChart();

        // initializeFilters();

        // initializeSearch();


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


        // ***************** RESPONSIVE TABLE COLUMNS *****************

        courseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        idColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.06));

        courseCodeColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.10));

        courseNameColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.20));

        scheduleColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.12));

        courseFeeColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.12));

        studentsCountColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.12));

        statusColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.11));

        actionsColumn.prefWidthProperty().bind(courseTable.widthProperty().multiply(0.13));

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

            Label label = new Label(
                    item.getName() + " (" + (int)item.getPieValue() + ")"
            );

            label.getStyleClass().add("legend-label");

            row.getChildren().addAll(circle, label);

            legendContainer.getChildren().add(row);

            i++;

        }

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
