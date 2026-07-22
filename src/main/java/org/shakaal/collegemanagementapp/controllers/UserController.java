package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import org.shakaal.collegemanagementapp.dao.UserDAO;

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

import org.shakaal.collegemanagementapp.models.User;
import org.shakaal.collegemanagementapp.session.Session;


public class UserController implements Initializable{


    //FXML CONTROLS

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Button addUserButton;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, LocalDate> createdAtColumn;

    @FXML
    private TableColumn<User, Void> actionsColumn;

    private final UserDAO userDAO = new UserDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        configureColumns();

        loadUsers();

        configureActionColumn();

        addUserButton.setOnAction(event -> openAddUserWindow());

        userTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // RESPONSIVE TABLE COLUMNS   **********

        idColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.06));

        usernameColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.14));

        fullNameColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.14));

        roleColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.10));

        createdAtColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.14));

        actionsColumn.prefWidthProperty().bind(userTable.widthProperty().multiply(0.16));

        Image image = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/search.png"));

        searchIcon.setImage(image);

    }

    private void configureColumns() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));


    }

    private void loadUsers() {


        ObservableList<User> users = userDAO.getAllUsers();

        System.out.println("Users loaded = " + users.size());

        userTable.setItems(users);

        totalUsersLabel.setText("Total Users : " + users.size());


    }

    private void configureActionColumn() {

        actionsColumn.setCellFactory(param ->
                new TableCell<>() {

                    private final Button deleteButton = new Button();

                    private final HBox buttons = new HBox( deleteButton);

                    {
                        deleteButton.getStyleClass().add("delete-button");

                        //Image view ICONS for edit button

                        //Image view ICONS for delete button

                        Image deleteImage = new Image(getClass().getResourceAsStream("/org/shakaal/collegemanagementapp/icons/delete.png"));

                        ImageView deleteView = new ImageView(deleteImage);
                        deleteView.setFitWidth(20);
                        deleteView.setFitHeight(20);

                        deleteButton.setGraphic(deleteView);

                        buttons.setAlignment(Pos.CENTER);

                        deleteButton.setOnAction(event -> {

                            User user =
                                    getTableView().getItems().get(getIndex());

                            deleteUser(user);

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

    private void openAddUserWindow() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/shakaal/collegemanagementapp/fxml/add-user.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Student");

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            refreshUsers();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private void updateTotalStudents() {

        totalUsersLabel.setText("Total Students: " + userTable.getItems().size());
    }

    private void refreshUsers() {

        loadUsers();
    }

    private void deleteUser(User user) {

        if (user.getUserId() == Session.getCurrentUser().getUserId()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Operation Not Allowed");

            alert.setHeaderText(null);

            alert.setContentText("You cannot delete the account you are currently logged in with.");

            alert.showAndWait();

            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Delete Student");

        confirmation.setHeaderText(null);

        confirmation.setContentText("Are you sure you want to delete " + user.getFullName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            UserDAO userDAO = new UserDAO();

            boolean deleted = userDAO.deleteUser(user.getUserId());

            if (deleted) {

                Alert success = new Alert(Alert.AlertType.INFORMATION);

                success.setTitle("Success");

                success.setHeaderText(null);

                success.setContentText("Student deleted successfully.");

                success.showAndWait();

                refreshUsers();
            }
        }
    }

}
