module com.example.dealershipics {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;

    opens com.example.dealershipics to javafx.fxml;
    exports com.example.dealershipics;
}