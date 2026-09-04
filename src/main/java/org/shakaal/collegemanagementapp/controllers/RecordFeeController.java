package org.shakaal.collegemanagementapp.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import org.shakaal.collegemanagementapp.dao.PaymentMethodDAO;
import org.shakaal.collegemanagementapp.dao.StudentFeePaymentDAO;
import org.shakaal.collegemanagementapp.models.PaymentMethod;
import org.shakaal.collegemanagementapp.models.Student;
import org.shakaal.collegemanagementapp.models.StudentFeePayment;
import javafx.collections.ObservableList;
import org.shakaal.collegemanagementapp.dao.CourseDAO;
import org.shakaal.collegemanagementapp.models.Course;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class RecordFeeController {


    // =========================================================
// FXML FIELDS
// =========================================================

    @FXML
    private ImageView collegeLogo;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField courseField;

    @FXML
    private TextField courseFeeField;

    @FXML
    private Label summaryPaymentDateLabel;

    @FXML
    private Label summaryReferenceLabel;

    @FXML
    private ComboBox<YearMonth> paymentMonthComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;

    @FXML
    private TextField paymentDateField;

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

    private final StudentFeePaymentDAO studentFeePaymentDAO = new StudentFeePaymentDAO();

    private final PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();

    private final CourseDAO courseDAO = new CourseDAO();


    // =========================================================
    // SELECTED STUDENT
    // =========================================================

    private Student student;


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
    // RECEIVE SELECTED STUDENT
    // =========================================================

    public void setStudent(Student student) {

        this.student = student;

        displayStudentInformation();

        displayCourseFee();

    }


    // =========================================================
    // DISPLAY STUDENT INFORMATION
    // =========================================================

    private void displayStudentInformation() {

        if (student == null) {
            return;
        }

        studentNameField.setText(
                student.getFirstName() + " " +
                        student.getLastName()
        );

        studentIdField.setText(
                String.valueOf(student.getStudentId())
        );

        courseField.setText(
                student.getCourseName()
        );
    }


    // =========================================================
// DISPLAY COURSE FEE
// =========================================================

    private void displayCourseFee() {

        if (student == null) {

            return;
        }

        ObservableList<Course> courses =
                courseDAO.getAllCourses();

        for (Course course : courses) {

            if (course.getCourseId() ==
                    student.getCourseId()) {

                courseFeeField.setText(
                        String.format(
                                "$%,.2f",
                                course.getCourseFee()
                        )
                );

                return;
            }
        }

        // Course could not be found
        courseFeeField.setText("N/A");
    }


    // =========================================================
    // PAYMENT MONTH
    // =========================================================

    private void setupPaymentMonthComboBox() {

        YearMonth currentMonth = YearMonth.now();

        List<YearMonth> months = new java.util.ArrayList<>();

        /*
         * Show several months around the current period.
         *
         * Current month first, followed by previous months.
         */

        for (int i = 0; i < 12; i++) {

            months.add(currentMonth.minusMonths(i));

        }

        paymentMonthComboBox.setItems(
                FXCollections.observableArrayList(months)
        );

        paymentMonthComboBox.setValue(currentMonth);

        paymentMonthComboBox.setConverter(
                new javafx.util.StringConverter<>() {

                    private final DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("MMMM yyyy");


                    @Override
                    public String toString(YearMonth month) {

                        if (month == null) {

                            return "";

                        }

                        return month.format(formatter);
                    }


                    @Override
                    public YearMonth fromString(String string) {

                        if (string == null || string.isBlank()) {

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
                paymentMethodDAO.getAllPaymentMethods();

        paymentMethodComboBox.setItems(
                FXCollections.observableArrayList(
                        paymentMethods
                )
        );

        /*
         * Display method name instead of object.toString().
         */

        paymentMethodComboBox.setCellFactory(
                listView -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            PaymentMethod item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(item.getMethodName());

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

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(item.getMethodName());

                        }

                    }

                }
        );

    }


    // =========================================================
    // PAYMENT DATE
    // =========================================================

    private void setPaymentDate() {

        String today = LocalDate.now().toString();

        paymentDateField.setText(today);

        //summaryPaymentDateLabel.setText(today);
    }


    // =========================================================
    // REFERENCE NUMBER
    // =========================================================

    private void generateReferenceNumber() {

        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        String reference =
                "FEE-" +
                        LocalDate.now() +
                        "-" +
                        randomPart;

        referenceNumberField.setText(reference);

        //summaryReferenceLabel.setText(reference);
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
    // SAVE PAYMENT
    // =========================================================

    private void savePayment() {

        // =====================================================
        // STUDENT CHECK
        // =====================================================

        if (student == null) {

            showError(
                    "No Student Selected",
                    "No student has been selected for this payment."
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
                    "Please select the month this fee payment is for."
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
                    "Please enter the payment amount."
            );

            amountField.requestFocus();

            return;
        }


        double amount;

        try {

            amount = Double.parseDouble(amountText);

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
                    "Payment amount must be greater than zero."
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
                studentFeePaymentDAO.hasStudentPaidForMonth(
                        student.getStudentId(),
                        paymentMonth
                );

        if (alreadyPaid) {

            showError(
                    "Payment Already Recorded",
                    student.getFirstName() +
                            " " +
                            student.getLastName() +
                            " has already paid the student fee for " +
                            paymentMonth.format(
                                    DateTimeFormatter.ofPattern("MMMM yyyy")
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
        // CREATE PAYMENT OBJECT
        // =====================================================

        StudentFeePayment payment =
                new StudentFeePayment();

        payment.setStudentId(
                student.getStudentId()
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

        payment.setReferenceNumber(referenceNumberField.getText());

        payment.setNotes(
                notes.isEmpty()
                        ? null
                        : notes
        );


        // =====================================================
        // SAVE THROUGH DAO
        // =====================================================

        boolean saved = studentFeePaymentDAO.addStudentFeePayment(payment);


        // =====================================================
        // RESULT
        // =====================================================

        if (saved) {

            showSuccess();

            closeWindow();

        } else {

            showError(
                    "Payment Failed",
                    "The student fee payment could not be recorded."
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

        alert.setTitle("Payment Recorded");

        alert.setHeaderText(null);

        alert.setContentText(
                "Student fee payment recorded successfully.\n\n" +
                        "Reference: " +
                        referenceNumberField.getText()
        );

        /*
         * We will replace this with your project's
         * global alert styling if needed.
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