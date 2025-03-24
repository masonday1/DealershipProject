package company.gui;

import java.awt.RenderingHints;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import javafiles.Key;
import javafiles.domainfiles.Dealership;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import company.gui.ProfileManagementController.DealershipRow;
import static company.gui.FXMLPath.*;


public class ProfileManagementController {
    private DealershipRow selectedDealershipRow;
    List<Map<Key, Object>> dataMap;

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

    @FXML
    public void initialize() {
        // Set up the columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        receivingColumn.setCellValueFactory(new PropertyValueFactory<>("receivingEnabled"));
        rentingColumn.setCellValueFactory(new PropertyValueFactory<>("rentingEnabled"));

        // Fetch the dealership rows from AppStateManager
        ObservableList<DealershipRow> tableData = FXCollections.observableArrayList(AppStateManager.getDealershipRows());

        // Set the table data
        dealershipTable.setItems(tableData);

        // Add a listener to the selected item in the table
        dealershipTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDealershipRow = newValue;
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(MAIN_SCREEN);
    }

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
                AppStateManager.getCompany().findDealership(selectedDealershipRow.getId()).setName(newName);
                
                // Refresh the table to reflect the change
                dealershipTable.refresh(); 
                System.out.println("Updated dealership name to: " + newName);
            });
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership,");

        }
    }



    @FXML
    private void handleAddDealership(ActionEvent event) {
        System.out.println("Add a Dealership button clicked");

        String dealershipId = null;
        while (dealershipId == null || dealershipId.trim().isEmpty()) {
            // Prompt the user for the dealership ID
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setTitle("Add Dealership");
            idDialog.setHeaderText("Enter the ID for the new dealership");
            idDialog.setContentText("Dealership ID:");

            Optional<String> idResult = idDialog.showAndWait();
            if (idResult.isPresent()) {
                dealershipId = idResult.get().trim();
                if (dealershipId.isEmpty()) {
                    showErrorAlert("Invalid Input", "Dealership ID is required. Please enter a valid ID.");
                }
            } else {
                // User canceled the dialog
                return;
            }
        }

        // Prompt the user for the dealership name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Dealership");
        nameDialog.setHeaderText("Enter the name for the new dealership");
        nameDialog.setContentText("Dealership Name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        final String finalDealershipId = dealershipId; // Make dealershipId effectively final
        nameResult.ifPresent(dealershipName -> {
            final String finalDealershipName = dealershipName; // Make dealershipName effectively final

            // Create a new DealershipRow and add it to the table
            DealershipRow newRow = new DealershipRow(finalDealershipId, finalDealershipName, true, false);
            dealershipTable.getItems().add(newRow);

            // Create a new Dealership object and add it to the Company object
            Dealership dealership = new Dealership(finalDealershipId, finalDealershipName);
            AppStateManager.addADealership(dealership);

            System.out.println("Added new dealership: ID = " + finalDealershipId + ", Name = " + finalDealershipName);
        });
    }


    /**
     * Handles the "Change Receiving Status" button action, toggling the receiving status of the selected dealership.
     */
    @FXML
    private void handleChangeReceivingStatus() {
        if (selectedDealershipRow != null) {
            Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipRow.getId());
            if (dealership != null) {
                boolean newStatus = !selectedDealershipRow.getReceivingEnabled();
                AppStateManager.setDealershipReceivingStatus(dealership,newStatus);
                selectedDealershipRow.setReceivingEnabled(newStatus);
                dealershipTable.refresh();
            }
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership,");
        }
    }

    /**
     * Handles the "Change Rental Status" button action, toggling the rental status of the selected dealership.
     */
    @FXML
    private void handleChangeRentalStatus() {
        if (selectedDealershipRow != null) {
            Dealership dealership = AppStateManager.getCompany().findDealership(selectedDealershipRow.getId());
            if (dealership != null) {
                boolean newStatus = !selectedDealershipRow.getRentingEnabled();
                AppStateManager.setDealershipRentalStatus(dealership,newStatus);
                selectedDealershipRow.setRentingEnabled(newStatus);
                dealershipTable.refresh();
            }
        } else {
            showErrorAlert("No Dealership Selected", "Please select a dealership,");
        }
    }





    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

        public void setReceivingEnabled(Boolean receivingStatus) {
            this.receivingEnabled = receivingStatus;
        }

        public void setRentingEnabled(Boolean rentalStatus)
        {
            this.rentingEnabled = rentalStatus;
        }

        public Boolean getRentingEnabled() {
            return rentingEnabled;
        }
    }
}