module com.example.laborator78 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.laborator78 to javafx.fxml;
    exports com.example.laborator78;
    exports com.example.laborator78.controller;
    opens com.example.laborator78.controller to javafx.fxml;
    exports com.example.laborator78.domain;
    opens com.example.laborator78.domain to javafx.fxml;
}