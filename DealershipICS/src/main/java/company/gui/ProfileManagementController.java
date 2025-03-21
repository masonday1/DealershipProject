package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;


public class ProfileManagementController
{
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        SceneManager sceneManager = SceneManager.getInstance(stage);
        sceneManager.switchScene("mainScene");

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