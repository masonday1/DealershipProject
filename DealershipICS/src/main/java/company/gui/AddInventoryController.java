package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static company.gui.FXMLPath.*;


/**
 * Controller for the "Add Inventory" screen.
 * Handles actions related to adding vehicle inventory, including loading from a file and manual entry.
 */
public class AddInventoryController {


    /**
     * Handles the "Back" button action.
     * Switches the scene to the inventory screen.
     *
     * @param event The ActionEvent triggered by the "Back" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }


    /**
     * Handles the "Load From File" button action.
     * Switches the scene to the "Add From File" screen.
     *
     * @param event The ActionEvent triggered by the "Load From File" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleLoadFromFile(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_FROM_FILE);
    }

    /**
     * Handles the "Enter Manually" button action.
     * Switches the scene to the vehicle entry screen.
     *
     * @param event The ActionEvent triggered by the "Enter Manually" button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleEnterManually(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(VEHICLE_ENTRY);
    }
}
