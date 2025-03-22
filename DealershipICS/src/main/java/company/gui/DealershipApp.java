package company.gui;


import javafx.application.Application;
import javafx.stage.Stage;

import static company.gui.FXMLPaths.*;

import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import java.util.List;
import java.util.Map;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.JSONIO;

public class DealershipApp extends Application {
    
    private static boolean initialLaunch = true;
    private static Company company = new Company();
    private static String masterInventoryList = "masterInventoryList.json";

    @Override
    public void start(Stage primaryStage) throws Exception {

        FileIOBuilder.setupFileIOBuilders();

        if (initialLaunch) {
            loadInitialFiles(company);
        }

        SceneManager sceneManger = SceneManager.getInstance(primaryStage);
        sceneManger.switchScene(MAIN_SCREEN);
        primaryStage.show();
    }


    private static void loadInitialFiles(Company company) throws ReadWriteException{
        initialLaunch = false;
        Path file = Paths.get(masterInventoryList);

        if (Files.exists(file)) {
            try {
                if (Files.size(file) == 0) {
                    System.out.println("File is empty: " + masterInventoryList);
                } else {
                    System.out.println("File found and is not empty: " + masterInventoryList);
                    readData();
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

    private static List<Map<Key, Object>> readData() throws ReadWriteException{
        List<Map<Key, Object>> data;
        
        JSONIO jsonIO = new JSONIO(masterInventoryList, 'r');
        data = jsonIO.readInventory();
        data.addAll(jsonIO.readInventory());
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }
}