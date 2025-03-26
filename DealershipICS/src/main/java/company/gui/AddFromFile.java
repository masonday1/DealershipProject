package company.gui;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIO;
import javafiles.dataaccessfiles.FileIOBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static company.gui.FXMLPath.ADD_FROM_FILE;
import static company.gui.FXMLPath.ADD_INVENTORY;

/**
 * Controller class for the "Add From File" functionality in the GUI.
 * This class handles the logic for importing inventory data from a file,
 * processing the data, and displaying any errors or invalid entries to the user.
 */
public class AddFromFile {

    /**
     * Initializes the controller and processes the file import.
     * This method prompts the user to select a file, reads the inventory data from the file,
     * and processes the data using {@link AppStateManager#dataToInventory(List)}.
     * If any invalid data is found, it displays the errors in a Swing JTable within a JFrame.
     */
    public void initialize() {
        String path = FileIOBuilder.selectFilePath('r');

        if (path == null) {return;}

        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(path, 'r');
            List<Map<Key, Object>> maps = fileIO.readInventory();
            List<Map<Key, Object>> badMaps = AppStateManager.dataToInventory(maps);

            if (badMaps != null && !badMaps.isEmpty()) {
                List<Map<String, Object>> keyData = GuiUtility.createKeyData();

                JTable jTable = GuiUtility.createTableFromMapList(badMaps, keyData);


                JScrollPane pane = new JScrollPane(jTable);
                JFrame jFrame = new JFrame();
                jFrame.getContentPane().add(pane);

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                jFrame.setSize( (int) screenBounds.getWidth(), (int) (screenBounds.getHeight()/ 4) );

                jFrame.setVisible(true);


                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the action when the back button is clicked.
     * Switches the scene to the add inventory screen.
     *
     * @param event The ActionEvent triggered by the back button.
     * @throws IOException If an I/O error occurs during scene switching.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_INVENTORY);
    }


    /**
     * Handles the "Add Another File" button action.
     * This method switches the current scene to the "Add From File" scene,
     * allowing the user to import another file.
     *
     * @param actionEvent The ActionEvent triggered by the "Add Another File" button.
     * @throws IOException If an I/O error occurs while switching scenes.
     */
    public void handleAnother(ActionEvent actionEvent) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_FROM_FILE);
    }
}
