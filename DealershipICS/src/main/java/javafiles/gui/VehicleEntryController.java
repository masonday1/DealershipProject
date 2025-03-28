package javafiles.gui;

import javafiles.customexceptions.*;
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

import static javafiles.gui.FXMLPath.ADD_INVENTORY;

/**
 * Controller class for the vehicle entry form.
 * Handles user interactions for adding vehicle information.
 */
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

    /**
     * Initializes the controller class.
     * Populates the dealershipComboBox with available dealership IDs.
     *
     * @param url   The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate dealershipComboBox
        dealershipComboBox.getItems().addAll(AppStateManager.getDealershipIDs());
    }

    /**
     * Handles the action when the back button is clicked.
     * Switches the scene to the add inventory screen.
     *
     * @param event The ActionEvent triggered by the back button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
      SceneManager sceneManager = SceneManager.getInstance(null);
      sceneManager.switchScene(ADD_INVENTORY);
    }



    /**
     * Handles the action when the add vehicle button is clicked.
     * Validates input fields and adds a new vehicle using AppStateManager.
     *
     * @param event The ActionEvent triggered by the add vehicle button.
     * @throws DealershipNotSelectedException If no dealership is selected.
     * @throws InvalidLongPriceException      If the vehicle price is invalid.
     * @throws InvalidAcquisitionDateException If the acquisition date is invalid.
     */
    @FXML
    private void handleAddVehicleButton(ActionEvent event) throws DealershipNotSelectedException, InvalidLongPriceException, InvalidAcquisitionDateException {
        // Validation (required fields)
        if (vehicleTypeField.getText().isEmpty() || vehicleIdField.getText().isEmpty() ||
                vehicleModelField.getText().isEmpty() || vehiclePriceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Missing Required Vehicle Information");
            return;
        }

        try
        {
        // Get input values, handling empty strings for optional fields
        String dealerId = dealershipComboBox.getValue();
        String vehicleId = vehicleIdField.getText().trim().replaceAll("\\s+", "");
        String vehicleManufacturer = vehicleManufacturerField.getText().isEmpty() ? null : vehicleManufacturerField.getText();
        String vehicleModel = vehicleModelField.getText();
        String vehicleType = vehicleTypeField.getText();
        String acquisitionDateStr = acquisitionDateField.getText();
        Long acquisitionDate = acquisitionDateStr.isEmpty() ? null : parseAcquisitionDate(acquisitionDateStr);
        String vehiclePriceStr = vehiclePriceField.getText();
        Long vehiclePrice = parseVehiclePrice(vehiclePriceStr);
        String priceUnit = priceUnitField.getText().isEmpty() ? null : priceUnitField.getText();


            if (dealerId == null) {
                throw new DealershipNotSelectedException("No dealership has been selected yet");
            }


            // Call AppStateManager to add the vehicle
            AppStateManager.manualVehicleAdd(dealerId, vehicleId, vehicleManufacturer, vehicleModel, vehiclePrice, acquisitionDate, vehicleType, priceUnit);

            JOptionPane.showMessageDialog(null, "Vehicle ID " + vehicleId + " has been successfully added");
            resetFields();

        } catch (VehicleAlreadyExistsException | InvalidPriceException |
                 DealershipNotAcceptingVehiclesException | InvalidVehicleTypeException |
                 InvalidAcquisitionDateException | InvalidLongPriceException |
                 DealershipNotSelectedException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }


    /**
     * Handles the action when the reset button is clicked.
     * Clears all input fields.
     *
     * @param event The ActionEvent triggered by the reset button.
     */
    @FXML
    private void handleResetButton(ActionEvent event) {
        resetFields();
    }

    /**
     * Resets all input fields to their default empty state.
     */
    private void resetFields() {
        vehicleTypeField.clear();
        vehicleIdField.clear();
        vehicleModelField.clear();
        vehiclePriceField.clear();
        vehicleManufacturerField.clear();
        acquisitionDateField.clear();
        priceUnitField.clear();
    }

    /**
     * Parses a string representing a vehicle price into a Long.
     *
     * @param priceStr The string to parse.
     * @return The parsed vehicle price as a Long.
     * @throws InvalidLongPriceException If the string cannot be parsed into a Long.
     */
    private Long parseVehiclePrice(String priceStr) throws InvalidLongPriceException {
        try {
            return Long.parseLong(priceStr);
        } catch (NumberFormatException e) {
            throw new InvalidLongPriceException("Invalid format for vehicle price. Please enter a valid integer number.");
        }
    }

    /**
     * Parses a string representing an acquisition date into a Long.
     *
     * @param dateStr The string to parse.
     * @return The parsed acquisition date as a Long.
     * @throws InvalidAcquisitionDateException If the string cannot be parsed into a Long.
     */
    private Long parseAcquisitionDate(String dateStr) throws InvalidAcquisitionDateException {
        try {
            return Long.parseLong(dateStr);
        } catch (NumberFormatException e) {
            throw new InvalidAcquisitionDateException("Invalid format for acquisition date. Please enter a valid Epoch time number.");
        }
    }

}