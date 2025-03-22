package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static company.gui.FXMLPaths.*;


public class ProfileManagementController
{

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);

    }
    @FXML
    private void handleEditDealershipName(ActionEvent event) {
        System.out.println("Edit Dealership Name button clicked");
        // Add logic to edit dealership name
    }

    @FXML
    private void handleAddDealershipName(ActionEvent event) {
        System.out.println("Add Dealership Name button clicked");
        // Add logic to add dealership name
    }

    @FXML
    private void handleAddDealership(ActionEvent event) {
        System.out.println("Add a Dealership button clicked");
        // Add logic to add a dealership
    }
}