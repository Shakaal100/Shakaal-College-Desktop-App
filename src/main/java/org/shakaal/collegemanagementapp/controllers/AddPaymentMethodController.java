package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.PaymentMethodDAO;
import org.shakaal.collegemanagementapp.models.PaymentMethod;


public class AddPaymentMethodController {


    // =========================================================
    // FXML FIELDS
    // =========================================================

    @FXML
    private ImageView collegeLogo;

    @FXML
    private TextField methodNameField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    // =========================================================
    // DAO
    // =========================================================

    private final PaymentMethodDAO paymentMethodDAO =
            new PaymentMethodDAO();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    private void initialize() {

        setupButtons();

    }


    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        saveButton.setOnAction(
                event -> savePaymentMethod()
        );

        cancelButton.setOnAction(
                event -> closeWindow()
        );

    }


    // =========================================================
    // SAVE PAYMENT METHOD
    // =========================================================

    private void savePaymentMethod() {

        // =====================================================
        // METHOD NAME
        // =====================================================

        String methodName =
                methodNameField.getText().trim();


        if (methodName.isEmpty()) {

            showError(
                    "Payment Method Required",
                    "Please enter a payment method."
            );

            methodNameField.requestFocus();

            return;
        }


        // =====================================================
        // CREATE PAYMENT METHOD OBJECT
        // =====================================================

        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setMethodName(methodName);


        // =====================================================
        // SAVE THROUGH DAO
        // =====================================================

        boolean saved = paymentMethodDAO.addPaymentMethod(paymentMethod);


        // =====================================================
        // RESULT
        // =====================================================

        if (saved) {

            showSuccess();

            closeWindow();

        } else {

            showError(
                    "Payment Method Failed",
                    "The payment method could not be added."
            );

        }

    }


    // =========================================================
    // SUCCESS
    // =========================================================

    private void showSuccess() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle("Payment Method Added");

        alert.setHeaderText(null);

        alert.setContentText(
                "Payment method \"" +
                        methodNameField.getText().trim() +
                        "\" was added successfully."
        );

        /*
         * Keep this consistent with the project's
         * global alert styling.
         */

        alert.showAndWait();

    }


    // =========================================================
    // ERROR ALERT
    // =========================================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/shakaal/collegemanagementapp/css/global.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("alert-dialog");

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        /*
         * Keep this consistent with the global
         * alert styling used by the project.
         */

        alert.showAndWait();

    }


    // =========================================================
    // CLOSE WINDOW
    // =========================================================

    private void closeWindow() {

        Stage stage =
                (Stage) cancelButton
                        .getScene()
                        .getWindow();

        stage.close();

    }

}