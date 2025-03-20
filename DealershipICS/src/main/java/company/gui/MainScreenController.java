package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;


/**
 * Controller for the main application screen.
 * Manages navigation and application closure.
 */
public class MainScreenController {

    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;
    @FXML
    private Button LoadInventoryButton;




    @FXML
    private void handleManageCompanyInventory(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/InventoryScreen.fxml");

    }

    @FXML
    private void handleManageCompanyProfile(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/ProfileManagement.fxml");
    }

    @FXML
    private void handleLoadInventory(ActionEvent event)
    {

        GuiUtility.navigateToScreen(event,"/LoadInventory.fxml");

    }


}