module com.example.imageprocessorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.imageprocessorapp to javafx.fxml;
    exports com.example.imageprocessorapp;
}