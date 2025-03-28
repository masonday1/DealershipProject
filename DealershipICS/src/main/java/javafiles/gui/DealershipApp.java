package javafiles.gui;


import javafiles.Key;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static javafiles.gui.FXMLPath.*;

/**
 * The main application class for the Dealership application.
 * This class extends the JavaFX Application class and handles the application's lifecycle,
 * including initialization, startup, and shutdown.
 */
public class DealershipApp extends Application {

    /**
     * Called when the application is stopped.
     * Writes the current inventory data to a file using {@link AppStateManager#writeToInventory()}.
     */
    @Override
    public void stop() {
        AppStateManager.writeToInventory();
    }

    /**
     * Called when the application is started.
     * Initializes the Company instance, loads initial data, and displays the main screen.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // create and intialize company instance
        Company company = new Company();

        AppStateManager.initializeCompany(company);

        FileIOBuilder.setupFileIOBuilders();
        List<Map<Key, Object>> badDataMaps = AppStateManager.loadInitialFiles();
        if (!badDataMaps.isEmpty()) {
            List<Map<String, Object>> keyData =GuiUtility.createKeyData();
            JTable jTable = GuiUtility.createTableFromMapList(badDataMaps,keyData);

            JScrollPane pane = new JScrollPane(jTable);
            JFrame jFrame = new JFrame();
            jFrame.getContentPane().add(pane);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            jFrame.setSize( (int) screenBounds.getWidth(), (int) (screenBounds.getHeight()/ 4) );

            jFrame.setVisible(true);

            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        SceneManager sceneManger = SceneManager.getInstance(primaryStage);
        sceneManger.switchScene(MAIN_SCREEN);
        primaryStage.show();
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}