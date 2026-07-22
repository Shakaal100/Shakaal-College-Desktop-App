package org.shakaal.collegemanagementapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/shakaal/collegemanagementapp/fxml/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("College Management System");

        stage.setScene(scene);

        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}