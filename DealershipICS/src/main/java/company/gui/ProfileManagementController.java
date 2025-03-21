package company.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static company.gui.FXMLPaths.*;
import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ProfileManagementController
{
    @FXML
    private ListView<String> dealershipListView;
    
    // private Company company;

    // public void setCompany(Company company) {
    //     this.company = company;
    //     populateDealershipList();
    // }

//     private void populateDealershipList() {
//     if (company != null && company.getDealershipIdList() != null) {
//         // Populate the ListView with the list of Dealership objects
//         ObservableList<String> dealerships = FXCollections.observableArrayList(company.getDealershipIdList());
//         dealershipListView.setItems(dealerships);
//     } else {
//         System.out.println("Company or dealership list is null.");
//     }
// }

// @FXML
// public void initialize() {
//     // Initialization logic that does not depend on the company object
//     dealershipListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//         if (newValue != null) {
//             System.out.println("Selected Dealership: " + newValue);
//             // Add logic to handle the selected dealership
//         }
//     });
// }

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
}