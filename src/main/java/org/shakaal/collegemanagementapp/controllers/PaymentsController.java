package org.shakaal.collegemanagementapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;


public class PaymentsController {

    @FXML
    private ScrollPane paymentsScrollPane;




    @FXML
    private void showStudentFees() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/shakaal/collegemanagementapp/fxml/student-fee-payments.fxml"
                    )
            );

            Node studentFeesContent = loader.load();

            paymentsScrollPane.setContent(studentFeesContent);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
