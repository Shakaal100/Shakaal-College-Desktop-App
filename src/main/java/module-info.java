module org.shakaal.collegemanagementapp {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports org.shakaal.collegemanagementapp;

    opens org.shakaal.collegemanagementapp to javafx.fxml;

    opens org.shakaal.collegemanagementapp.controllers to javafx.fxml;

    opens org.shakaal.collegemanagementapp.models to javafx.base;

}