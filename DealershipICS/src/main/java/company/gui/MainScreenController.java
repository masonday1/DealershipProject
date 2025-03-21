package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.event.ActionEvent;

import javafiles.domainfiles.Company;

import java.io.IOException;
import static company.gui.FXMLPaths.*;

import javafx.fxml.FXMLLoader;


/**
 * Controller for the main application screen.
 * Manages navigation and application closure.
 */
public class MainScreenController {

    @FXML
    private Menu menuLabel;
    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;
    @FXML
    private Button LoadInventoryButton;

    private Company company;

    public void setCompany(Company company) {
        this.company = company;
        System.out.println("Company object set in MainScreenController.");
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

    @FXML
    private void handleLoadInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(LOAD_INVENTORY);

    }


}