package company.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.io.IOException;
import static company.gui.FXMLPaths.*;


/**
 * Controller for the inventory management screen.
 * Handles actions related to vehicle inventory.
 */
public class InventoryScreenController
{


    @FXML
    private void handleBack(ActionEvent event) throws IOException
        {
            SceneManager sceneManager = SceneManager.getInstance(null);
            sceneManager.switchScene(MAIN_SCREEN);

        }

    @FXML
    private void handleAddVehicleToDealership(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_INVENTORY);


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