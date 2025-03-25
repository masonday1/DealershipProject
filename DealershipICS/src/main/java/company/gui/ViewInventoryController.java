package company.gui;

import javafiles.domainfiles.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;


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
}
