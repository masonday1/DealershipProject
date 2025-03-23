package company.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TextInputDialog;

import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;
import javafiles.Key;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import company.gui.ProfileManagementController.DealershipRow;

import static company.gui.FXMLPaths.*;


public class ProfileManagementController
{
    private static Company company;
    private DealershipRow selectedDealershipRow;

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

        // Make the table editable
        dealershipTable.setEditable(true);

        // Make the name column editable
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            // Update the name in the selected row
            DealershipRow row = event.getRowValue();
            row.setName(event.getNewValue());
            System.out.println("Updated dealership name to: " + event.getNewValue());
        });

        // Populate the table
        List<Map<Key, Object>> dataMap = company.getDataMap();
        ObservableList<DealershipRow> tableData = FXCollections.observableArrayList();

        // Check if dataMap is null and handle it
        if (dataMap != null) {
            for (Map<Key, Object> map : dataMap) {
                String id = (String) map.get(Key.DEALERSHIP_ID);
                String name = (String) map.get(Key.DEALERSHIP_NAME);
                Boolean receivingEnabled = (Boolean) map.get(Key.DEALERSHIP_RECEIVING_STATUS);
                Boolean rentingEnabled = (Boolean) map.get(Key.DEALERSHIP_RENTING_STATUS);

                // Check if the ID already exists in tableData
                boolean idExists = tableData.stream().anyMatch(row -> row.getId().equals(id));
                if (!idExists) {
                    tableData.add(new DealershipRow(id, name, receivingEnabled, rentingEnabled));
                }
            }
        }

        // Set the table data (empty if dataMap was null)
        dealershipTable.setItems(tableData);

        // Add a listener to the selected item in the table
        dealershipTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Store the selected row's data for use in other methods
                selectedDealershipRow = newValue;
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);
    }
    
    // Todo: Handle duplicate dealership IDs
    @FXML
    private void handleEditDealershipName(ActionEvent event) {
        if (selectedDealershipRow != null) {
            System.out.println("Edit Dealership Name button clicked for: " + selectedDealershipRow.getId());

            // Create a TextInputDialog to get the new name from the user
            TextInputDialog dialog = new TextInputDialog(selectedDealershipRow.getName());
            dialog.setTitle("Edit Dealership Name");
            dialog.setHeaderText("Edit the name of the selected dealership");
            dialog.setContentText("Enter new name:");

            // Show the dialog and capture the result
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                // Update the name in the selected row
                selectedDealershipRow.setName(newName);
                company.findDealership(selectedDealershipRow.getId()).setName(newName);
                // TODO update company info with new name

                dealershipTable.refresh(); // Refresh the table to reflect the change
                System.out.println("Updated dealership name to: " + newName);
            });
        } else {
            System.out.println("No dealership selected.");
        }
    }

    // TODO Is this necessary since handleEditDealershipName allows the name to be changed if blank
    @FXML
    private void handleAddDealershipName(ActionEvent event) {
        System.out.println("Add Dealership Name button clicked");
        // Add logic to add dealership name
    }

    @FXML
private void handleAddDealership(ActionEvent event) {
    System.out.println("Add Dealership button clicked");

    // Prompt the user for the dealership ID
    TextInputDialog idDialog = new TextInputDialog();
    idDialog.setTitle("Add Dealership");
    idDialog.setHeaderText("Enter the ID for the new dealership");
    idDialog.setContentText("Dealership ID:");

    Optional<String> idResult = idDialog.showAndWait();
    idResult.ifPresent(dealershipId -> {
        // Prompt the user for the dealership name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Dealership");
        nameDialog.setHeaderText("Enter the name for the new dealership");
        nameDialog.setContentText("Dealership Name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        nameResult.ifPresent(dealershipName -> {
            // Create a new DealershipRow and add it to the table
            DealershipRow newRow = new DealershipRow(dealershipId, dealershipName, false, false);
            dealershipTable.getItems().add(newRow);

            // TODO: Add the new dealership to the Company object
            // *Currently not functioning correctly
            Dealership newDealership = new Dealership(dealershipId, dealershipName);
            company.addDealership(newDealership);

            System.out.println("Added new dealership: ID = " + dealershipId + ", Name = " + dealershipName);
        });
    });
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

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getReceivingEnabled() {
            return receivingEnabled;
        }

        public Boolean getRentingEnabled() {
            return rentingEnabled;
        }
    }
}