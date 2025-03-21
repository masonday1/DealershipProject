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
    private void handleManageCompanyInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    @FXML
    private void handleManageCompanyProfile(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene( PROFILE_MANAGEMENT);
    }

    @FXML
    private void handleLoadInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(LOAD_INVENTORY);

    }
}
