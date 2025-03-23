package company.gui;

import javafiles.customexceptions.DealershipNotAcceptingVehiclesException;
import javafiles.customexceptions.InvalidPriceException;
import javafiles.customexceptions.InvalidVehicleTypeException;
import javafiles.customexceptions.VehicleAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.swing.*;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import static company.gui.FXMLPath.ADD_INVENTORY;

public class VehicleEntryController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> dealershipComboBox;

    @FXML
    private Button addVehicleButton;

    @FXML
    private Button resetButton;

    @FXML
    private TextField vehicleTypeField;

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TextField vehicleModelField;

    @FXML
    private TextField vehiclePriceField;

    @FXML
    private TextField vehicleManufacturerField;

    @FXML
    private TextField acquisitionDateField;

    @FXML
    private TextField priceUnitField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate dealershipComboBox
        dealershipComboBox.getItems().addAll(AppStateManager.getDealershipIDs());
    }


    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
      SceneManager sceneManager = SceneManager.getInstance(null);
      sceneManager.switchScene(ADD_INVENTORY);
    }

    @FXML
    private void handleAddVehicleButton(ActionEvent event) {
        // Validation
        if (vehicleTypeField.getText().isEmpty() || vehicleIdField.getText().isEmpty() ||
                vehicleModelField.getText().isEmpty() || vehiclePriceField.getText().isEmpty()) {
            // Display error message (e.g., using an alert)
            JOptionPane.showMessageDialog(null, "Missing Required Vehicle Information");
            return;
        }

        // Get input values
        String dealerId = dealershipComboBox.getValue();
        String vehicleId = vehicleIdField.getText();
        String vehicleManufacturer = vehicleManufacturerField.getText();
        String vehicleModel = vehicleModelField.getText();
        String vehicleType = vehicleTypeField.getText();
        String acquisitionDateStr = acquisitionDateField.getText();
        String vehiclePriceStr = vehiclePriceField.getText();
        String priceUnit = priceUnitField.getText();

        try {
            // Parse input values
            Long vehiclePrice = Long.parseLong(vehiclePriceStr);
            Long acquisitionDate = Long.parseLong(acquisitionDateStr);

            // Call AppStateManager to add the vehicle
            AppStateManager.manualVehicleAdd(dealerId, vehicleId, vehicleManufacturer, vehicleModel, vehiclePrice, acquisitionDate, vehicleType, priceUnit);

            System.out.println("Vehicle added successfully.");
            // Reset fields
            resetFields();


        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid format for price.");
        }
        catch (VehicleAlreadyExistsException e)
        {
            JOptionPane.showMessageDialog(null, "Vehicle ID already exists in inventory");
        }
        catch (InvalidPriceException e)
        {
            JOptionPane.showMessageDialog(null, "Price is invalid, please enter an integer greater than 0");
        }
        catch (DealershipNotAcceptingVehiclesException e)
        {
            JOptionPane.showMessageDialog(null, "Dealership is not currently accepting vehicles");
        }
        catch (InvalidVehicleTypeException e)
        {
            JOptionPane.showMessageDialog(null,"Invalid vehicle type, valid vehicle types are SUV, Sedan, Sports Car, Pickup");
        }

        // Reset Fields
        resetFields();
    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        resetFields();
    }

    private void resetFields() {
        vehicleTypeField.clear();
        vehicleIdField.clear();
        vehicleModelField.clear();
        vehiclePriceField.clear();
        vehicleManufacturerField.clear();
        acquisitionDateField.clear();
        priceUnitField.clear();
    }
}