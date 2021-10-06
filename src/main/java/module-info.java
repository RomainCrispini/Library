module fr.romain.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens fr.romain.library.entity to javafx.base;
    opens fr.romain.library to javafx.fxml;
    exports fr.romain.library;
    exports fr.romain.library.controller;
    opens fr.romain.library.controller to javafx.fxml;
}