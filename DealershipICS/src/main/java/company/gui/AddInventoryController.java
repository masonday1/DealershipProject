package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AddInventoryController {


    @FXML
    private void handleBack(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/InventoryScreen.fxml");

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
