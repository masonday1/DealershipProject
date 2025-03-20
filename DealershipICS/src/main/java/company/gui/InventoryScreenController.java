package company.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;


/**
 * Controller for the inventory management screen.
 * Handles actions related to vehicle inventory.
 */
public class InventoryScreenController
{




    @FXML
    private void handleBack(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/MainScreen.fxml");
    }

    @FXML
    private void handleAddVehicleToDealership(ActionEvent event) {
        GuiUtility.navigateToScreen(event,"/AddInventory.fxml");

    }

    @FXML
    private void handleRemoveVehicleFromDealership() {
        System.out.println("Remove vehicle clicked");
    }

    @FXML
    private void handleTransferVehicleBetweenDealerships() {
        System.out.println("Transfer vehicle clicked");
    }

    @FXML
    private void handleEnableVehicleRental() {
        System.out.println("Enable rental clicked");
    }

    @FXML
    private void handleDisableVehicleRental() {
        System.out.println("Disable rental clicked");
    }
}