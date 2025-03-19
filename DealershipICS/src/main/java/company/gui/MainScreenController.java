package company.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.application.Platform;

/**
 * Controller for the main application screen.
 * Manages navigation and application closure.
 */
public class MainScreenController {

    /**
     * Button to navigate to the inventory management screen.
     */
    @FXML
    private Button manageCompanyInventoryButton;

    /**
     * Button to navigate to the company profile management screen.
     */
    @FXML
    private Button ManageCompanyProfileButton;

    /**
     * Button to load inventory data.
     */
    @FXML
    private Button LoadInventoryButton;

    /**
     * Button to close the application.
     */
    @FXML
    private Button closeButton;

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     */
    @FXML
    public void initialize() {
        // Initialization logic (currently empty)
    }

    /**
     * Handles the action when the "Manage Company Inventory" button is clicked.
     * Loads and displays the inventory management screen.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleManageCompanyInventory(ActionEvent event) {
        System.out.println("Manage Inventory button clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryScreen.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the "Manage Company Profile" button is clicked.
     * Opens the company profile management screen.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleManageCompanyProfile(ActionEvent event) {
        System.out.println("Modify Dealership Profile button clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileManagement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the "Load Inventory" button is clicked.
     * Loads inventory data.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleLoadInventory(ActionEvent event)
    {
        System.out.println("Load Inventory button was clicked");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoadInventory.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the "Close" button is clicked.
     * Exits the application.
     *
     * @param event The action event triggered by the button click.
     */
    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }
}
