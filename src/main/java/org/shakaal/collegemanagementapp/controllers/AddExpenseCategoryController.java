package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.ExpenseCategoryDAO;
import org.shakaal.collegemanagementapp.models.ExpenseCategory;

public class AddExpenseCategoryController {


    // =========================================================
    // FXML FIELDS
    // =========================================================

    @FXML
    private ImageView collegeLogo;

    @FXML
    private TextField categoryNameField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    // =========================================================
    // DAO
    // =========================================================

    private final ExpenseCategoryDAO expenseCategoryDAO =
            new ExpenseCategoryDAO();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        setupButtons();

    }


    // =========================================================
    // SETUP BUTTONS
    // =========================================================

    private void setupButtons() {

        saveButton.setOnAction(
                event -> handleSave()
        );

        cancelButton.setOnAction(
                event -> handleCancel()
        );

    }


    // =========================================================
    // SAVE EXPENSE CATEGORY
    // =========================================================

    private void handleSave() {

        // -----------------------------------------------------
        // GET CATEGORY NAME
        // -----------------------------------------------------

        String categoryName =
                categoryNameField.getText().trim();


        // -----------------------------------------------------
        // VALIDATE CATEGORY NAME
        // -----------------------------------------------------

        if (categoryName.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Category Name",
                    "Please enter an expense category name."
            );

            categoryNameField.requestFocus();

            return;
        }


        // -----------------------------------------------------
        // CREATE EXPENSE CATEGORY OBJECT
        // -----------------------------------------------------

        ExpenseCategory category =
                new ExpenseCategory();

        category.setCategoryName(categoryName);


        // -----------------------------------------------------
        // SAVE TO DATABASE
        // -----------------------------------------------------

        boolean saved =
                expenseCategoryDAO.addExpenseCategory(category);


        // -----------------------------------------------------
        // CHECK RESULT
        // -----------------------------------------------------

        if (saved) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Category Added",
                    "The expense category was added successfully."
            );

            closeWindow();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Save Failed",
                    "The expense category could not be added. Please try again."
            );

        }

    }


    // =========================================================
    // CANCEL
    // =========================================================

    private void handleCancel() {

        closeWindow();

    }


    // =========================================================
    // CLOSE WINDOW
    // =========================================================

    private void closeWindow() {

        Stage stage =
                (Stage) cancelButton.getScene().getWindow();

        stage.close();

    }


    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType alertType,
            String title,
            String message
    ) {

        Alert alert = new Alert(alertType);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

    }

}