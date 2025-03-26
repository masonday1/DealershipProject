package company.gui;


import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;


import java.io.IOException;

import static company.gui.FXMLPath.*;



/**
 * Controller for the inventory management screen.
 * Handles actions related to vehicle inventory.
 */
public class InventoryScreenController
{


    public Button btnChangeRental;
    public Button btnTransferVehicle;
    public Button btnRemoveVehicle;
    public Button btnAddVehicle;
    public Button btnViewInventory;

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
    private void handleRemoveVehicleFromDealership() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_REMOVAL);
    }

    @FXML
    private void handleTransferVehicleBetweenDealerships() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_TRANSFER);
    }

    @FXML
    private void handleChangeVehicleRental() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHIClE_RENTAL);
    }


    @FXML
    private void handleViewCompanyInventory() throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VIEW_INVENTORY);
    }

    }



