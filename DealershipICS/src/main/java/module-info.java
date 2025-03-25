module com.example.dealershipics {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires java.desktop;

    opens company.gui to javafx.fxml;
    exports company.gui;
    opens javafiles.domainfiles to javafx.base;
}