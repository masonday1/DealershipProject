package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.util.List;
import java.util.Map;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.dataaccessfiles.JSONIO;
import javafiles.domainfiles.Company;

import static company.gui.FXMLPaths.*;
import javafiles.dataaccessfiles.FileIO;

public class MainScreenController {

    private static boolean initialLaunch = true;
    private static String masterInventoryList = "masterInventoryList.json";
    private static Company company = new Company();

    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;
    @FXML
    private Button LoadInventoryButton;

    @FXML
    public void initialize() {
        // Move this code segment to a new class
        try {
            FileIOBuilder.setupFileIOBuilders();

            if (initialLaunch) {
                System.out.println("initial launch");
                loadInitialFiles();
                initialLaunch = false;
            }

            ProfileManagementController.setCompany(company);

        } catch (ReadWriteException ex) {
            System.err.println("An error occurred during initialization: " + ex.getMessage());
        }
        // TODO: Implement loading initial file class
        
    }

    @FXML
    private void handleManageCompanyInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(INVENTORY_SCREEN);
    }

    @FXML
    private void handleManageCompanyProfile(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene( PROFILE_MANAGEMENT);
    }

    @FXML
    private void handleLoadInventory(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(LOAD_INVENTORY);

    }
    // Move this code segment to a new class
    private void loadInitialFiles() throws ReadWriteException {
        Path file = Paths.get(masterInventoryList);

        if (Files.exists(file)) {
            try {
                if (Files.size(file) == 0) {
                    System.out.println("File is empty: " + masterInventoryList);
                } else {
                    System.out.println("File found and is not empty: " + masterInventoryList);
                    FileIO jsonIO = new JSONIO(masterInventoryList, 'r');
                    List<Map<Key, Object>> data = jsonIO.readInventory();
                    data.addAll(jsonIO.readInventory());
                    company.dataToInventory(data);
                }
            } catch (IOException ex) {
                System.err.println("An error occurred while checking the file size: " + ex.getMessage());
            }
        } else {
            try {
                File newFile = new File(masterInventoryList);
                if (newFile.createNewFile()) {
                    System.out.println(masterInventoryList + " created successfully.");
                } else {
                    System.out.println("Failed to create " + masterInventoryList);
                }
            } catch (IOException ex) {
                System.err.println("An error occurred while creating the file: " + ex.getMessage());
            }
        }
    }
}
