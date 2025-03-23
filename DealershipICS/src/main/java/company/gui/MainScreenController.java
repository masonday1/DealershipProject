package company.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.dataaccessfiles.FileIO;
import javafiles.domainfiles.Company;

import static company.gui.FXMLPaths.*;

public class MainScreenController {

    private static boolean initialLaunch = true;
    private static String masterInventoryList = "masterInventoryList.json";
    private static Company company = new Company();

    public Company getCompany() {return company;}


    @FXML
    private Button manageCompanyInventoryButton;
    @FXML
    private Button ManageCompanyProfileButton;
    @FXML
    private Button LoadInventoryButton;

    @FXML
    public void initialize() {
        // Move this code segment to a new class

        if (initialLaunch) {
            FileIOBuilder.setupFileIOBuilders();
            System.out.println("initial launch");
            List<Map<Key, Object>> badDataMaps= loadInitialFiles();
            ProfileManagementController.setCompany(company);
            initialLaunch = false;
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

    private List<Map<Key, Object>> loadInitialFiles() {
        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(masterInventoryList, 'r');
            return company.dataToInventory(fileIO.readInventory());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
