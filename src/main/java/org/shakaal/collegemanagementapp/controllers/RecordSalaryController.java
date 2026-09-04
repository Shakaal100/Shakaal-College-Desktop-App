package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.shakaal.collegemanagementapp.dao.PaymentMethodDAO;
import org.shakaal.collegemanagementapp.dao.TeacherSalaryPaymentDAO;
import org.shakaal.collegemanagementapp.models.PaymentMethod;
import org.shakaal.collegemanagementapp.models.Teacher;
import org.shakaal.collegemanagementapp.models.TeacherSalaryPayment;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


public class RecordSalaryController {


    // =========================================================
    // FXML FIELDS
    // =========================================================

    @FXML
    private ImageView collegeLogo;

    @FXML
    private TextField teacherNameField;

    @FXML
    private TextField teacherIdField;

    @FXML
    private TextField specializationField;

    @FXML
    private TextField salaryField;

    @FXML
    private ComboBox<YearMonth> paymentMonthComboBox;

    @FXML
    private TextField paymentDateField;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextField referenceNumberField;

    @FXML
    private TextArea notesArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    // =========================================================
    // DAO
    // =========================================================

    private final TeacherSalaryPaymentDAO teacherSalaryPaymentDAO =
            new TeacherSalaryPaymentDAO();

    private final PaymentMethodDAO paymentMethodDAO =
            new PaymentMethodDAO();


    // =========================================================
    // SELECTED TEACHER
    // =========================================================

    private Teacher teacher;


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    private void initialize() {

        setupPaymentMonthComboBox();

        setupPaymentMethodComboBox();

        setupButtons();

        generateReferenceNumber();

        setPaymentDate();

    }


    // =========================================================
    // RECEIVE SELECTED TEACHER
    // =========================================================

    public void setTeacher(Teacher teacher) {

        this.teacher = teacher;

        displayTeacherInformation();

    }


    // =========================================================
    // DISPLAY TEACHER INFORMATION
    // =========================================================

    private void displayTeacherInformation() {

        if (teacher == null) {

            return;
        }


        teacherNameField.setText(
                teacher.getFullName()
        );


        teacherIdField.setText(
                String.valueOf(
                        teacher.getTeacherID()
                )
        );


        specializationField.setText(
                teacher.getSpecialization()
        );


        salaryField.setText(
                String.format(
                        "$%,.2f",
                        teacher.getSalary()
                )
        );

    }


    // =========================================================
    // PAYMENT MONTH
    // =========================================================

    private void setupPaymentMonthComboBox() {

        YearMonth currentMonth = YearMonth.now();

        List<YearMonth> months =
                new java.util.ArrayList<>();


        /*
         * Show the current month followed
         * by the previous 11 months.
         */

        for (int i = 0; i < 12; i++) {

            months.add(
                    currentMonth.minusMonths(i)
            );

        }


        paymentMonthComboBox.setItems(
                FXCollections.observableArrayList(
                        months
                )
        );


        paymentMonthComboBox.setValue(
                currentMonth
        );


        paymentMonthComboBox.setConverter(
                new javafx.util.StringConverter<>() {

                    private final DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern(
                                    "MMMM yyyy"
                            );


                    @Override
                    public String toString(
                            YearMonth month
                    ) {

                        if (month == null) {

                            return "";

                        }

                        return month.format(
                                formatter
                        );

                    }


                    @Override
                    public YearMonth fromString(
                            String string
                    ) {

                        if (
                                string == null ||
                                        string.isBlank()
                        ) {

                            return null;

                        }

                        return YearMonth.parse(
                                string,
                                formatter
                        );

                    }

                }
        );

    }


    // =========================================================
    // PAYMENT METHOD
    // =========================================================

    private void setupPaymentMethodComboBox() {

        List<PaymentMethod> paymentMethods =
                paymentMethodDAO
                        .getAllPaymentMethods();


        paymentMethodComboBox.setItems(
                FXCollections.observableArrayList(
                        paymentMethods
                )
        );


        /*
         * Display method name instead of
         * PaymentMethod.toString().
         */

        paymentMethodComboBox.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            PaymentMethod item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );


                        if (
                                empty ||
                                        item == null
                        ) {

                            setText(null);

                        } else {

                            setText(
                                    item.getMethodName()
                            );

                        }

                    }

                }
        );


        paymentMethodComboBox.setButtonCell(
                new ListCell<>() {

                    @Override
                    protected void updateItem(
                            PaymentMethod item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );


                        if (
                                empty ||
                                        item == null
                        ) {

                            setText(null);

                        } else {

                            setText(
                                    item.getMethodName()
                            );

                        }

                    }

                }
        );

    }


    // =========================================================
    // PAYMENT DATE
    // =========================================================

    private void setPaymentDate() {

        String today =
                LocalDate.now().toString();


        paymentDateField.setText(
                today
        );

    }


    // =========================================================
    // REFERENCE NUMBER
    // =========================================================

    private void generateReferenceNumber() {

        /*
         * Reference number is generated
         * automatically by the system.
         */

        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();


        String reference =
                "SAL-" +
                        LocalDate.now() +
                        "-" +
                        randomPart;


        referenceNumberField.setText(
                reference
        );

    }


    // =========================================================
    // BUTTONS
    // =========================================================

    private void setupButtons() {

        saveButton.setOnAction(
                event -> savePayment()
        );


        cancelButton.setOnAction(
                event -> closeWindow()
        );

    }


    // =========================================================
    // SAVE SALARY PAYMENT
    // =========================================================

    private void savePayment() {


        // =====================================================
        // TEACHER CHECK
        // =====================================================

        if (teacher == null) {

            showError(
                    "No Teacher Selected",
                    "No teacher has been selected for this salary payment."
            );

            return;
        }


        // =====================================================
        // PAYMENT MONTH
        // =====================================================

        YearMonth paymentMonth =
                paymentMonthComboBox.getValue();


        if (paymentMonth == null) {

            showError(
                    "Payment Month Required",
                    "Please select the month this salary payment is for."
            );

            return;
        }


        // =====================================================
        // AMOUNT
        // =====================================================

        String amountText =
                amountField.getText().trim();


        if (amountText.isEmpty()) {

            showError(
                    "Amount Required",
                    "Please enter the salary payment amount."
            );

            amountField.requestFocus();

            return;
        }


        double amount;


        try {

            amount =
                    Double.parseDouble(
                            amountText
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Invalid Amount",
                    "Please enter a valid numeric amount."
            );

            amountField.requestFocus();

            return;
        }


        if (amount <= 0) {

            showError(
                    "Invalid Amount",
                    "Salary payment amount must be greater than zero."
            );

            amountField.requestFocus();

            return;
        }


        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        PaymentMethod paymentMethod =
                paymentMethodComboBox.getValue();


        if (paymentMethod == null) {

            showError(
                    "Payment Method Required",
                    "Please select a payment method."
            );

            paymentMethodComboBox.requestFocus();

            return;
        }


        // =====================================================
        // CHECK DUPLICATE PAYMENT
        // =====================================================

        boolean alreadyPaid =
                teacherSalaryPaymentDAO
                        .hasTeacherBeenPaidForMonth(
                                teacher.getTeacherID(),
                                paymentMonth
                        );


        if (alreadyPaid) {

            showError(
                    "Salary Already Recorded",

                    teacher.getFullName() +
                            " has already received a salary payment for " +
                            paymentMonth.format(
                                    DateTimeFormatter.ofPattern(
                                            "MMMM yyyy"
                                    )
                            ) +
                            "."
            );

            return;
        }


        // =====================================================
        // NOTES
        // =====================================================

        String notes =
                notesArea.getText().trim();


        // =====================================================
        // CREATE SALARY PAYMENT OBJECT
        // =====================================================

        TeacherSalaryPayment payment =
                new TeacherSalaryPayment();


        payment.setTeacherId(
                teacher.getTeacherID()
        );


        payment.setPaymentMonth(
                paymentMonth
        );


        payment.setAmount(
                amount
        );


        payment.setPaymentDate(
                LocalDate.now()
        );


        payment.setPaymentMethodId(
                paymentMethod.getPaymentMethodId()
        );


        payment.setReferenceNumber(
                referenceNumberField.getText()
        );


        payment.setNotes(
                notes.isEmpty()
                        ? null
                        : notes
        );


        // =====================================================
        // SAVE THROUGH DAO
        // =====================================================

        boolean saved =
                teacherSalaryPaymentDAO
                        .addTeacherSalaryPayment(
                                payment
                        );


        // =====================================================
        // RESULT
        // =====================================================

        if (saved) {

            showSuccess();

            closeWindow();

        } else {

            showError(
                    "Payment Failed",
                    "The teacher salary payment could not be recorded."
            );

        }

    }


    // =========================================================
    // SUCCESS
    // =========================================================

    private void showSuccess() {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Salary Payment Recorded"
        );


        alert.setHeaderText(null);


        alert.setContentText(

                "Teacher salary payment recorded successfully.\n\n" +

                        "Teacher: " +
                        teacher.getFullName() +

                        "\nReference: " +
                        referenceNumberField.getText()

        );


        /*
         * We can replace this with the project's
         * global alert styling if necessary.
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

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(null);


        alert.setContentText(
                message
        );


        /*
         * Keep this consistent with the
         * project's global alert styling.
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