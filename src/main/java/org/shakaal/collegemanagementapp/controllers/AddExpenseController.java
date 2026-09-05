package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.shakaal.collegemanagementapp.dao.CollegeExpenseDAO;
import org.shakaal.collegemanagementapp.dao.ExpenseCategoryDAO;
import org.shakaal.collegemanagementapp.dao.PaymentMethodDAO;
import org.shakaal.collegemanagementapp.models.CollegeExpense;
import org.shakaal.collegemanagementapp.models.ExpenseCategory;
import org.shakaal.collegemanagementapp.models.PaymentMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AddExpenseController {

    // ============================================================
    // FXML FIELDS
    // ============================================================

    @FXML
    private ImageView collegeLogo;

    @FXML
    private ComboBox<ExpenseCategory> expenseCategoryComboBox;

    @FXML
    private TextField expenseDateField;

    @FXML
    private TextField referenceNumberField;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    // ============================================================
    // DAOs
    // ============================================================

    private final CollegeExpenseDAO collegeExpenseDAO =
            new CollegeExpenseDAO();

    private final ExpenseCategoryDAO expenseCategoryDAO =
            new ExpenseCategoryDAO();

    private final PaymentMethodDAO paymentMethodDAO =
            new PaymentMethodDAO();


    // ============================================================
    // INITIALIZE
    // ============================================================

    @FXML
    public void initialize() {

        setupExpenseCategories();

        setupPaymentMethods();

        setupExpenseDate();

        generateReferenceNumber();

        setupButtons();
    }


    // ============================================================
    // SETUP EXPENSE CATEGORIES
    // ============================================================

    private void setupExpenseCategories() {

        List<ExpenseCategory> categories =
                expenseCategoryDAO.getAllExpenseCategories();

        expenseCategoryComboBox.getItems().setAll(categories);

        expenseCategoryComboBox.setConverter(
                new javafx.util.StringConverter<ExpenseCategory>() {

                    @Override
                    public String toString(ExpenseCategory category) {

                        if (category == null) {
                            return "";
                        }

                        return category.getCategoryName();
                    }

                    @Override
                    public ExpenseCategory fromString(String string) {

                        return null;
                    }
                }
        );
    }


    // ============================================================
    // SETUP PAYMENT METHODS
    // ============================================================

    private void setupPaymentMethods() {

        List<PaymentMethod> paymentMethods =
                paymentMethodDAO.getAllPaymentMethods();

        paymentMethodComboBox.getItems().setAll(paymentMethods);

        paymentMethodComboBox.setConverter(
                new javafx.util.StringConverter<PaymentMethod>() {

                    @Override
                    public String toString(PaymentMethod paymentMethod) {

                        if (paymentMethod == null) {
                            return "";
                        }

                        return paymentMethod.getMethodName();
                    }

                    @Override
                    public PaymentMethod fromString(String string) {

                        return null;
                    }
                }
        );
    }


    // ============================================================
    // SETUP EXPENSE DATE
    // ============================================================

    private void setupExpenseDate() {

        expenseDateField.setText(
                LocalDate.now().toString()
        );
    }


    // ============================================================
    // GENERATE REFERENCE NUMBER
    // ============================================================

    private void generateReferenceNumber() {

        String referenceNumber =
                "EXP-" +
                        LocalDate.now() +
                        "-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase();

        referenceNumberField.setText(referenceNumber);
    }


    // ============================================================
    // SETUP BUTTONS
    // ============================================================

    private void setupButtons() {

        saveButton.setOnAction(
                event -> handleSave()
        );

        cancelButton.setOnAction(
                event -> handleCancel()
        );
    }


    // ============================================================
    // SAVE EXPENSE
    // ============================================================

    private void handleSave() {

        // --------------------------------------------------------
        // GET SELECTED CATEGORY
        // --------------------------------------------------------

        ExpenseCategory selectedCategory =
                expenseCategoryComboBox.getValue();

        if (selectedCategory == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Expense Category",
                    "Please select an expense category."
            );

            expenseCategoryComboBox.requestFocus();

            return;
        }


        // --------------------------------------------------------
        // GET SELECTED PAYMENT METHOD
        // --------------------------------------------------------

        PaymentMethod selectedPaymentMethod =
                paymentMethodComboBox.getValue();

        if (selectedPaymentMethod == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Payment Method",
                    "Please select a payment method."
            );

            paymentMethodComboBox.requestFocus();

            return;
        }


        // --------------------------------------------------------
        // VALIDATE AMOUNT
        // --------------------------------------------------------

        String amountText =
                amountField.getText().trim();

        if (amountText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Amount",
                    "Please enter the expense amount."
            );

            amountField.requestFocus();

            return;
        }


        double amount;

        try {

            amount = Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Amount",
                    "Please enter a valid numeric amount."
            );

            amountField.requestFocus();

            return;
        }


        if (amount <= 0) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Amount",
                    "Expense amount must be greater than zero."
            );

            amountField.requestFocus();

            return;
        }


        // --------------------------------------------------------
        // GET DESCRIPTION
        // --------------------------------------------------------

        String description =
                descriptionArea.getText().trim();


        // --------------------------------------------------------
        // CREATE COLLEGE EXPENSE OBJECT
        // --------------------------------------------------------

        CollegeExpense expense =
                new CollegeExpense();

        expense.setCategoryId(
                selectedCategory.getCategoryId()
        );

        expense.setAmount(amount);

        expense.setExpenseDate(
                LocalDate.now()
        );

        expense.setPaymentMethodId(
                selectedPaymentMethod.getPaymentMethodId()
        );

        expense.setReferenceNumber(
                referenceNumberField.getText()
        );

        expense.setDescription(
                description
        );


        // --------------------------------------------------------
        // SAVE TO DATABASE
        // --------------------------------------------------------

        boolean saved =
                collegeExpenseDAO.addExpense(expense);


        if (saved) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Expense Added",
                    "The college expense was added successfully."
            );

            closeWindow();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Save Failed",
                    "The expense could not be saved. Please try again."
            );
        }
    }


    // ============================================================
    // CANCEL
    // ============================================================

    private void handleCancel() {

        closeWindow();
    }


    // ============================================================
    // CLOSE WINDOW
    // ============================================================

    private void closeWindow() {

        Stage stage =
                (Stage) cancelButton.getScene().getWindow();

        stage.close();
    }


    // ============================================================
    // ALERT
    // ============================================================

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