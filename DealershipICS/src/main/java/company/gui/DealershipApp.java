package company.gui;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;

import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIO;


public class DealershipApp extends Application {
    private static boolean initialLaunch = true;
    private static String masterInventoryFile = "masterinventory.json"; // Default load location

    @Override
    public void start(Stage primaryStage) throws Exception {
        Company company = new Company("c_ID", "c_Name");

        FileIOBuilder.setupFileIOBuilders();
        loadInitialFiles(company);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    private static void loadInitialFiles(Company company) {
        try { 
            Scanner scanner = new Scanner(new File(masterInventoryFile)); 
            company.dataToInventory(readData(scanner));
            System.out.println("Reading JSON file..." + masterInventoryFile);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + masterInventoryFile + " file not found.");
            try {
                File newFile = new File(masterInventoryFile);
                if (newFile.createNewFile()) {
                    System.out.println(masterInventoryFile + "created successfully.");
                } else {
                    System.out.println("Failed to create " + masterInventoryFile);
                }
            } catch (IOException ioException) {
                System.out.println("An error occurred while creating masterinventory.json: " + ioException.getMessage());
            }
        }
    }

    private static List<Map<String, Object>> readData(Scanner sc) {
        // TODO: Check if data list is still needed, or if only readInventory is needed
        List<Map<String, Object>> data = new ArrayList<>();
        FileIO fileIO = openFile('r', sc);
        if (fileIO == null) {return null;}
        try {
            data.addAll(fileIO.readInventory());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    private static FileIO openFile(char mode, Scanner sc) {
        String path;
        FileIO fileIO = null;
        char userInput;

        try {
            mode = FileIO.getLowercaseMode(mode);
        } catch (ReadWriteException e) {
            System.out.println("Mode '" + mode + "' is not valid, returning null.");
            return null;
        }
        if (!initialLaunch) {

            do {
                System.out.print("Choose Path: ");
                path = FileIOBuilder.selectFilePath();
                try {
                    if (path != null) {
                        System.out.println(path + "\n");
                        fileIO = FileIOBuilder.buildNewFileIO(path, mode);
                    } else {
                        System.out.println("File chooser closed. No file opened.");
                        return null;
                    }
                } catch (ReadWriteException e) {
                    System.out.print(e.getMessage() + "\n\nEnter new path ('y' / 'n'): ");
                    userInput = Character.toLowerCase(sc.next().charAt(0));
                    while (userInput != 'y' && userInput != 'n') {
                        System.out.println("Invalid input, try again (only 'y' or 'n' valid): ");
                        userInput = Character.toLowerCase(sc.next().charAt(0));
                    }
                    if (userInput == 'n') {return null;}
                }
                } while (fileIO == null);
            }
            else {
                initialLaunch = true;
                System.out.println(masterInventoryFile + "\n");
                try {
                    fileIO = FileIOBuilder.buildNewFileIO(masterInventoryFile, mode);
                } catch (ReadWriteException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                    return null;
                }
            }
            return fileIO;
    }

    public static void main(String[] args) {  
        launch(args);
    }
}