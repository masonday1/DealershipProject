package javafiles.gui;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javafiles.gui.FXMLPath.ADD_FROM_FILE;
import static javafiles.gui.FXMLPath.ADD_INVENTORY;

/**
 * Controller class for the "Add From File" functionality in the GUI.
 * This class handles the logic for importing inventory data from a file,
 * processing the data, and displaying any errors or invalid entries to the user.
 */
public class AddFromFile {

    public void initialize() {
    String path = FileIOBuilder.selectFilePath('r');

    if (path == null) {
        return;
    }

    try {
        FileIO fileIO = FileIOBuilder.buildNewFileIO(path, 'r');
        List<Map<Key, Object>> maps = fileIO.readInventory();
        List<Map<Key, Object>> badMaps = AppStateManager.dataToInventory(maps);

        String[] columnNames = {"Vehicle ID", "Make", "Model", "Type", "Dealership ID"};
        List<Object[]> successData = new ArrayList<>();
        List<Object[]> invalidData = new ArrayList<>();

        for (Map<Key, Object> map : maps) {
            Object[] rowData = {
                map.get(Key.VEHICLE_ID),
                map.get(Key.VEHICLE_MANUFACTURER),
                map.get(Key.VEHICLE_MODEL),
                map.get(Key.VEHICLE_TYPE),
                map.get(Key.DEALERSHIP_ID)
            };

            if (badMaps.contains(map)) {
                invalidData.add(rowData);
            } else {
                successData.add(rowData);
            }
        }

        JTable successTable = new JTable(successData.toArray(new Object[0][]), columnNames);
        JTable invalidTable = new JTable(invalidData.toArray(new Object[0][]), columnNames);

        JScrollPane successScrollPane = new JScrollPane(successTable);
        successScrollPane.setBorder(BorderFactory.createTitledBorder("Successfully Added Vehicles"));
        successScrollPane.setPreferredSize(new java.awt.Dimension(500, 200));

        JScrollPane invalidScrollPane = new JScrollPane(invalidTable);
        invalidScrollPane.setBorder(BorderFactory.createTitledBorder("Unable to Add Vehicles"));
        invalidScrollPane.setPreferredSize(new java.awt.Dimension(500, 200));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(successScrollPane);
        panel.add(invalidScrollPane);

        JOptionPane.showMessageDialog(null, panel, "Vehicle Import Results", JOptionPane.INFORMATION_MESSAGE);

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
