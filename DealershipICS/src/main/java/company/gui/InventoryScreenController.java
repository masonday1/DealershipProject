package company.gui;

import javafiles.Key;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    @FXML
    private void handleViewCompanyInventory()
    {
        List<Map<Key, Object>> vehicleData = AppStateManager.getCompanyData();

        JTable vehicleTable = GuiUtility.createTableFromMapList(vehicleData);

        if (vehicleTable != null) {

            // remove column for error reason
            GuiUtility.removeColumnByName(vehicleTable,"error_reason");

            JFrame inventoryFrame = new JFrame("Vehicle Inventory");
            inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the inventory frame

            JScrollPane scrollPane = new JScrollPane(vehicleTable);
            inventoryFrame.getContentPane().add(scrollPane);

            inventoryFrame.pack();
            inventoryFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "No vehicle data available.");
        }
    }

    }



