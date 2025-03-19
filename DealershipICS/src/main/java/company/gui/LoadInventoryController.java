package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoadInventoryController {

    @FXML
    private void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleLoadFromFile(ActionEvent event) {
        System.out.println("Load From File button clicked");
        // Add logic to load inventory from a file
    }

    @FXML
    private void handleEnterManually(ActionEvent event) {
        System.out.println("Enter Manually button clicked");
        // Add logic to enter inventory manually
    }
}