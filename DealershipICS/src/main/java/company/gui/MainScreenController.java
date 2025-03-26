package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

import javafiles.domainfiles.Company;

import static company.gui.FXMLPath.*;

public class MainScreenController {

    private static Company company = new Company();

    public Company getCompany() {return company;}


    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;


    @FXML
    public void initialize() {
        // Move this code segment to a new class
        // TODO: Implement loading initial file class
        
    }

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
}
