package company.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the inventory management screen.
 * Handles actions related to vehicle inventory.
 */
public class InventoryScreenController {

    /**
     * Handles the action when the "Back" button is clicked.
     * Closes the current stage and returns to the previous screen.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Close the current stage
        stage.close();
    }

    /**
     * Handles the action when the "Add Vehicle to Dealership" button is clicked.
     * Implement logic to add a vehicle to the dealership's inventory.
     */
    @FXML
    private void handleAddVehicleToDealership() {
        // logic to add vehicle
        System.out.println("Add vehicle clicked");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadInventory.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Inventory");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Handles the action when the "Remove Vehicle from Dealership" button is clicked.
     * Implement logic to remove a vehicle from the dealership's inventory.
     */
    @FXML
    private void handleRemoveVehicleFromDealership() {
        // logic to remove vehicle
        System.out.println("Remove vehicle clicked");
    }

    /**
     * Handles the action when the "Transfer Vehicle Between Dealerships" button is clicked.
     * Implement logic to transfer a vehicle between dealerships.
     */
    @FXML
    private void handleTransferVehicleBetweenDealerships() {
        // logic to transfer vehicle between dealerships
        System.out.println("Transfer vehicle clicked");
    }

    /**
     * Handles the action when the "Enable Vehicle Rental" button is clicked.
     * Implement logic to enable vehicle rental.
     */
    @FXML
    private void handleEnableVehicleRental() {
        // logic to enable vehicle rental
        System.out.println("Enable rental clicked");
    }

    /**
     * Handles the action when the "Disable Vehicle Rental" button is clicked.
     * Implement logic to disable vehicle rental.
     */
    @FXML
    private void handleDisableVehicleRental() {
        // logic to disable vehicle rental
        System.out.println("Disable rental clicked");
    }
}
