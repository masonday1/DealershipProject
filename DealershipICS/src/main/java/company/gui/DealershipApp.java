package company.gui;


import javafx.application.Application;
import javafx.stage.Stage;

import static company.gui.FXMLPaths.*;

import javafiles.domainfiles.Company;

public class DealershipApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       SceneManager sceneManger = SceneManager.getInstance(primaryStage);
       sceneManger.switchScene(MAIN_SCREEN);
       primaryStage.show();
    }


    private static void loadInitialFiles(Company company) {
        // TODO: Create method to load saved data on application start
        // File found: Read masterInventoryFile.json → call Company.dataToInventory()
        // Not found: Create masterInventoryFile.json
        
        // Create class boolean initialLaunch = true
        // Set to false when loadInitialFiles is called  

    }

    public static void main(String[] args) {
        launch(args);
    }
}