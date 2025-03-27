module com.example.dealershipics {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;

    opens javafiles.gui to javafx.fxml;
    exports javafiles.gui;
    opens javafiles.domainfiles to javafx.base, org.mockito;
}