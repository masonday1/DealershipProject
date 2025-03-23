package company.gui;


import javafiles.Key;
import javafx.geometry.Rectangle2D;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.List;
import java.util.Map;



/**
 * A utility class that contains commonly used functions for managing GUI elements,
 * such as navigating between screens and setting the window size.
 */
public class GuiUtility {

    /**
     * Helper method to get the screen title based on the FXML path.
     *
     * @param fxmlPath The FXML path to determine the screen title.
     * @return The screen title corresponding to the given FXML path.
     */
    public static String getScreenTitle(String fxmlPath) {
        switch (fxmlPath) {
            case FXMLPaths.MAIN_SCREEN:
                return "Main Menu";
            case FXMLPaths.INVENTORY_SCREEN:
                return "Inventory Management";
            case FXMLPaths.LOAD_INVENTORY:
                return "Load Inventory";
            case FXMLPaths.PROFILE_MANAGEMENT:
                return "Profile Management";
            case FXMLPaths.ADD_INVENTORY:
                return "Add Inventory";
            case FXMLPaths.ADD_FROM_FILE:
                return "Load From File";
            default:
                return "Unknown Screen";
        }
    }


    /**
     * Sets the size of the stage and ensures the size is retained across scene changes.
     * This method calculates the window size as a percentage of the screen size, and
     * allows you to keep that size even after scene transitions.
     *
     * @param stage            The stage whose size is to be set.
     * @param widthPercentage  The percentage of the screen width to be used for the stage's width.
     * @param heightPercentage The percentage of the screen height to be used for the stage's height.
     */
    public static void setScreenSize(Stage stage, double widthPercentage, double heightPercentage) {
        // Get screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calculate the size based on the given percentage
        double stageWidth = screenWidth * widthPercentage;
        double stageHeight = screenHeight * heightPercentage;

        // Set the stage size
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        //set minimum size to 50% of the screen size or any other desired value
        stage.setMinWidth(screenWidth * 0.5);
        stage.setMinHeight(screenHeight * 0.5);
    }


    /**
     * Creates a JTable from a List of Maps.
     *
     * @param data The List of Maps containing vehicle data.
     * @return A JTable displaying the vehicle data, or null if the input list is null or empty.
     */
    public static JTable createTableFromMapList (List <Map<Key,Object>> data){
        if (data == null || data.isEmpty()) {
                return null;
        }

        // Use Key enum values for column names (maintains order)
        Key[] keys = Key.values();
        String[] columnNames = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            columnNames[i] = keys[i].getKey();
        }

        // Prepare table data
        Object[][] tableData = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            Map<Key, Object> rowMap = data.get(i);
            for (int j = 0; j < keys.length; j++) {
                tableData[i][j] = rowMap.get(keys[j]);
                }
            }

            // Create the JTable
            DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
            return new JTable(model);
        }


    /**
     * Removes a column from a JTable based on its header name.
     *
     * @param table      The JTable from which to remove the column.
     * @param columnName The header name of the column to remove.
     */
    public static void removeColumnByName(JTable table, String columnName) {
        if (table == null || columnName == null || columnName.isEmpty()) {
            return; // Handle null or empty inputs
        }

        TableColumnModel columnModel = table.getColumnModel();
        int columnIndexToRemove = -1;

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            if (columnModel.getColumn(i).getHeaderValue().equals(columnName)) {
                columnIndexToRemove = i;
                break;
            }
        }

        if (columnIndexToRemove >= 0) {
            TableColumn columnToRemove = columnModel.getColumn(columnIndexToRemove);
            columnModel.removeColumn(columnToRemove);
        } else {
            System.out.println("Column '" + columnName + "' not found in the table.");
        }
    }



    }
