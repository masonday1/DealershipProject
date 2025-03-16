package company.gui;

// DealershipController.java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class DealershipController {

    @FXML
    private Button loadInventoryButton;

    @FXML
    private Button modifyProfileButton;

    @FXML
    private Button modifyInventoryButton;

    @FXML
    public void initialize() {
        // Initialization logic
    }

    @FXML
    private void handleLoadInventory(ActionEvent event) {
        System.out.println("Load Inventory button clicked");
        // Add logic
    }

    @FXML
    private void handleModifyProfile(ActionEvent event) {
        System.out.println("Modify Dealership Profile button clicked");
        // add logic
    }

    @FXML
    private void handleModifyInventory(ActionEvent event) {
        System.out.println("Modify Dealership Inventory button clicked");
        // add logic
    }
}
