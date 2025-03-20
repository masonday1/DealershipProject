package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafiles.domainfiles.Company;
import javafx.event.ActionEvent;
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
    private void handleManageCompanyInventory(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/InventoryScreen.fxml");

    }

    @FXML
    private void handleManageCompanyProfile(ActionEvent event) {
        FXMLLoader loader = GuiUtility.navigateToScreen(event,"/ProfileManagement.fxml");
        ProfileManagementController controller = loader.getController();
        controller.setCompany(this.company); // Pass the Company object to the ProfileManagementController

    }

    @FXML
    private void handleLoadInventory(ActionEvent event)
    {

        GuiUtility.navigateToScreen(event,"/LoadInventory.fxml");

    }


}