package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;


public class PaymentsController {

    @FXML
    private ScrollPane paymentsScrollPane;

    @FXML
    private Button overviewButton;

    @FXML
    private Button studentFeesButton;

    @FXML
    private Button teacherSalariesButton;

    @FXML
    private Button expensesButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button addPaymentMethodButton;

    @FXML
    private Button addExpenseButton;

    @FXML
    private Button addExpenseCategoryButton;

    @FXML
    private void initialize() {

        addPaymentMethodButton.setOnAction(event -> openAddPaymentMethodWindow());

        addExpenseButton.setOnAction(event -> OpenAddExpense());

        addExpenseCategoryButton.setOnAction(event -> openAddExpenseCategoryWindow());

    }


    @FXML
    private void loadPaymentContent(String fxmlFile, Button activeButton) {

        try {

            // Change the blue active tab
            setActiveTab(activeButton);

            // Load the requested FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/shakaal/collegemanagementapp/fxml/" + fxmlFile
                    )
            );

            Node content = loader.load();

            // Put it into the existing ScrollPane
            paymentsScrollPane.setContent(content);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }




    @FXML
    private void showOverview() {

        loadPaymentContent("payments-overview.fxml", overviewButton);
    }



    @FXML
    private void showStudentFees() {

        loadPaymentContent(
                "student-fee-payments.fxml",
                studentFeesButton
        );
    }


    @FXML
    private void showTeacherSalaries() {

        loadPaymentContent(
                "teacher-salary-payments.fxml",
                teacherSalariesButton
        );
    }


    @FXML
    private void showExpenses() {

        loadPaymentContent(
                "expenses.fxml",
                expensesButton
        );
    }


    @FXML
    private void showTransactions() {

        loadPaymentContent(
                "transactions.fxml",
                transactionsButton
        );
    }


    @FXML
    private void showReports() {

        loadPaymentContent(
                "payment-reports.fxml",
                reportsButton
        );
    }



    private void setActiveTab(Button activeButton) {

        overviewButton.getStyleClass().remove("payment-tab-active");
        studentFeesButton.getStyleClass().remove("payment-tab-active");
        teacherSalariesButton.getStyleClass().remove("payment-tab-active");
        expensesButton.getStyleClass().remove("payment-tab-active");
        transactionsButton.getStyleClass().remove("payment-tab-active");
        reportsButton.getStyleClass().remove("payment-tab-active");

        activeButton.getStyleClass().add("payment-tab-active");
    }



    private void openAddPaymentMethodWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/shakaal/collegemanagementapp/fxml/add-payment-method.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Payment Method");

            Scene scene = new Scene(root, 500, 350);

            stage.setScene(scene);

            stage.setResizable(false);

            stage.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    @FXML
    private void OpenAddExpense() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/shakaal/collegemanagementapp/fxml/add-expense.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add College Expense");

            Scene scene = new Scene(root, 620, 520);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.showAndWait();

            // Refresh the Payments page after the expense window closes
           // loadStatistics();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openAddExpenseCategoryWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/shakaal/collegemanagementapp/fxml/add-expense-category.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Add Expense Category");

            Scene scene = new Scene(root, 500, 350);

            stage.setScene(scene);

            stage.setResizable(false);

            stage.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



}
