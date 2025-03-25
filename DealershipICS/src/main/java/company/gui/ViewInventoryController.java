package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;

import static company.gui.FXMLPath.INVENTORY_SCREEN;

/**
 * Controller class for the View Inventory screen.
 * Handles the display of vehicle inventory in a TableView.
 */
public class ViewInventoryController {

    @FXML
    private TableView<javafiles.domainfiles.Vehicle> tableView;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, Boolean> rentalColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleManufacturerColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML
    private TableColumn<Vehicle, Long> vehiclePriceColumn;
    @FXML
    private TableColumn<Vehicle, String> priceUnitColumn;
    @FXML
    private TableColumn<Vehicle, Long > acquisitionDateColumn;

    /**
     * Initializes the controller. Sets up the TableView columns and loads vehicle data.
     */
    @FXML
    public void initialize() {
        // Initialize the table columns with PropertyValueFactory
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        rentalColumn.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        vehicleManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleManufacturer"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("vehiclePrice"));
        priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("priceUnit"));
        acquisitionDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAcquisitionDate"));

        // Load data into the table
        loadVehicleData();
    }

    /**
     * Loads the vehicle data from AppStateManager and populates the TableView.
     */
    private void loadVehicleData() {
        ObservableList<javafiles.domainfiles.Vehicle> vehicleList = FXCollections.observableArrayList(AppStateManager.getListCompanyVehicles());
        tableView.setItems(vehicleList);
    }

    /**
     * Handles the "Back" button click event. Switches the scene to the inventory screen.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    /**
     * Inner static class representing a Vehicle.
     * This class is used to hold the data for each vehicle displayed in the TableView.
     */
    public static class Vehicle {
        private int vehicleId;
        private boolean rentable;
        private String type;
        private String manufacturer;
        private String model;
        private double price;
        private String priceUnit;
        private LocalDate acquisitionDate;

        /**
         * Constructs a new Vehicle object.
         *
         * @param vehicleId       The vehicle's ID.
         * @param rentable        Indicates if the vehicle is rentable.
         * @param type            The vehicle type.
         * @param manufacturer    The vehicle manufacturer.
         * @param model           The vehicle model.
         * @param price           The vehicle price.
         * @param priceUnit       The price unit.
         * @param acquisitionDate The acquisition date.
         */
        public Vehicle(int vehicleId, boolean rentable, String type, String manufacturer, String model, double price, String priceUnit, LocalDate acquisitionDate) {
            this.vehicleId = vehicleId;
            this.rentable = rentable;
            this.type = type;
            this.manufacturer = manufacturer;
            this.model = model;
            this.price = price;
            this.priceUnit = priceUnit;
            this.acquisitionDate = acquisitionDate;
        }

        // Getters and setters for all fields
        public int getVehicleId() { return vehicleId; }
        public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

        public boolean isRentable() { return rentable; }
        public void setRentable(boolean rentable) { this.rentable = rentable; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public String getPriceUnit() { return priceUnit; }
        public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }

        public LocalDate getAcquisitionDate() { return acquisitionDate; }
        public void setAcquisitionDate(LocalDate acquisitionDate) { this.acquisitionDate = acquisitionDate; }
    }
}
