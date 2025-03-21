package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

import static company.gui.FXMLPaths.*;

public class MainScreenController {

    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;
    @FXML
    private Button LoadInventoryButton;

    @FXML
    private void handleManageCompanyInventory(ActionEvent event) {
        SceneManager sceneManager = SceneManager.getInstance(null);  // Use the existing instance
        try {
            sceneManager.loadScene("inventoryScene", "/InventoryScreen.fxml");
            sceneManager.switchScene("inventoryScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageCompanyProfile(ActionEvent event) {
        SceneManager sceneManager = SceneManager.getInstance(null); // No need to pass the stage
        try {
            sceneManager.loadScene("profileScene", PROFILE_MANAGEMENT);
            sceneManager.switchScene("profileScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadInventory(ActionEvent event) {
        SceneManager sceneManager = SceneManager.getInstance(null); // No need to pass the stage
        try {
            sceneManager.loadScene("loadScene", LOAD_INVENTORY);
            sceneManager.switchScene("loadScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
