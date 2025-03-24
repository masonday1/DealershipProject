package company.gui;

import javafiles.customexceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static company.gui.FXMLPath.INVENTORY_SCREEN;
import static company.gui.FXMLPath.MAIN_SCREEN;

/**
 * Controller for the Vehicle Rental screen, handling vehicle rental operations.
 */
public class VehicleRentalController {

    @FXML
    private ComboBox<String> dealershipComboBox;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> vehicleIdColumn;

    @FXML
    private TableColumn<Vehicle, String> manufacturerColumn;

    @FXML
    private TableColumn<Vehicle, String> modelColumn;

    @FXML
    private TableColumn<Vehicle, Long> priceColumn;

    @FXML
    private TableColumn<Vehicle, Boolean> rentableColumn;

    private Vehicle selectedVehicle;

    /**
     * Initializes the controller, setting up the combo box and table.
     */
    @FXML
    public void initialize() {
        // Populate the combo box with dealerships that have renting enabled
        List<String> rentingEnabledDealershipIDs = AppStateManager.getRentingEnabledDealershipIDs();
        dealershipComboBox.setItems(FXCollections.observableArrayList(rentingEnabledDealershipIDs));

        // Set up the table columns
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("Rental"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        rentableColumn.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));

        // Add a listener to the selected item in the table
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedVehicle = newValue;
            }
        });

        //Add a listener to the combo box to update the table when a new dealership is selected.
        dealershipComboBox.setOnAction(this::handleDealershipSelection);
    }

    /**
     * Handles the "Back" button action, switching to the main screen.
     *
     * @param event The action event.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    /**
     * Handles the "Change Rental" button action, toggling the rental status of the selected vehicle.
     *
     * @param event The action event.
     */
    @FXML
    private void handleChangeRental(ActionEvent event) throws VehicleAlreadyExistsException, DealershipNotRentingException,
            VehicleNotRentableException, DealershipNotAcceptingVehiclesException, EmptyInventoryException {
        String selectedDealershipId = dealershipComboBox.getValue();
        if (selectedDealershipId == null) {
            showAlert("Please select a dealership.");
            return;
        }

        if (selectedVehicle == null) {
            showAlert("Please select a vehicle.");
            return;
        }

        Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipId);
        if (dealership == null) {
            showAlert("Dealership not found.");
            return;
        }

        //selectedVehicle.setRentable(!selectedVehicle.isRentable());
        vehicleTable.refresh();

        // Update the vehicle in the dealership's inventory
        AppStateManager.updateDealershipVehicleRentalState(selectedDealershipId,selectedVehicle);
    }

    /**
     * Displays an alert dialog with the given message.
     *
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the dealership selection event, updating the vehicle table with the selected dealership's vehicles.
     *
     * @param event The action event.
     */
    @FXML
    private void handleDealershipSelection(ActionEvent event) {
//        String selectedDealershipId = dealershipComboBox.getValue();
//        if (selectedDealershipId != null) {
//            Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipId);
//            if (dealership != null) {
//                //ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList(dealership.getVehicles());
//                vehicleTable.setItems(vehicleData);
//            }
//        }
    }


    // Inner class to represent a row in the table
    public static class VehicleRow {
        private String vehicleID;
        private String vehicleType;
        private Boolean rental;
        private String vehicleModel;
        private String vehicleManufacturer;

        public VehicleRow(String vehicleID, String vehicleType, Boolean rental, String vehicleModel, String vehicleManufacturer)
        {
            this.vehicleID = vehicleID;
            this.vehicleType = vehicleType;
            this.rental = rental;
            this.vehicleModel = vehicleModel;
            this.vehicleManufacturer = vehicleManufacturer;
        }

        public String getVehicleID(){return vehicleID;}
        public Boolean getRental(){return rental;}
        public void setRental(Boolean rentalStatus){this.rental = rentalStatus;}

    }

}