package company.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafiles.domainfiles.Company;
import javafiles.Key;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static company.gui.FXMLPath.*;


public class ProfileManagementController
{
    private static Company company;

    @FXML
    private TableView<DealershipRow> dealershipTable;

    @FXML
    private TableColumn<DealershipRow, String> idColumn;

    @FXML
    private TableColumn<DealershipRow, String> nameColumn;

    @FXML
    private TableColumn<DealershipRow, Boolean> receivingColumn;

    @FXML
    private TableColumn<DealershipRow, Boolean> rentingColumn;

    public static void setCompany(Company company) {
        ProfileManagementController.company = company;
    }

    @FXML
    public void initialize() {
        
        // Set up the columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        receivingColumn.setCellValueFactory(new PropertyValueFactory<>("receivingEnabled"));
        rentingColumn.setCellValueFactory(new PropertyValueFactory<>("rentingEnabled"));

        // Populate the table
        List<Map<Key, Object>> dataMap = company.getDataMap();
        ObservableList<DealershipRow> tableData = FXCollections.observableArrayList();

        for (Map<Key, Object> map : dataMap) {
            String id = (String) map.get(Key.DEALERSHIP_ID);
            String name = (String) map.get(Key.DEALERSHIP_NAME);
            Boolean receivingEnabled = (Boolean) map.get(Key.DEALERSHIP_RECEIVING_STATUS);
            Boolean rentingEnabled = (Boolean) map.get(Key.DEALERSHIP_RENTING_STATUS);

            tableData.add(new DealershipRow(id, name, receivingEnabled, rentingEnabled));
        }
        dealershipTable.setItems(tableData);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);
    }

    @FXML
    private void handleEditDealershipName(ActionEvent event) {
        System.out.println("Edit Dealership Name button clicked");
        // Add logic to edit dealership name
    }

    @FXML
    private void handleAddDealershipName(ActionEvent event) {
        System.out.println("Add Dealership Name button clicked");
        // Add logic to add dealership name
    }

    @FXML
    private void handleAddDealership(ActionEvent event) {
        System.out.println("Add a Dealership button clicked");
        // Add logic to add a dealership
    }

    // Inner class to represent a row in the table
    public static class DealershipRow {
        private String id;
        private String name;
        private Boolean receivingEnabled;
        private Boolean rentingEnabled;

        public DealershipRow(String id, String name, Boolean receivingEnabled, Boolean rentingEnabled) {
            this.id = id;
            this.name = name;
            this.receivingEnabled = receivingEnabled;
            this.rentingEnabled = rentingEnabled;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Boolean getReceivingEnabled() {
            return receivingEnabled;
        }

        public Boolean getRentingEnabled() {
            return rentingEnabled;
        }
    }
}