package company.gui;


import javafiles.Key;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.*;



/**
 * A utility class that contains commonly used functions for managing GUI elements,
 * such as navigating between screens and setting the window size.
 */
public class GuiUtility {

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

    public static Map<String, Object> createKeyDataPoint(Key key, String name, Integer width) {
        if (width == null) {
            width = 10;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("key", key);
        map.put("width", width);
        return map;
    }

    public static List<Map<String, Object>> createKeyData() {
        List<Map<String, Object>> keyData = new ArrayList<>();
        keyData.add(createKeyDataPoint(Key.DEALERSHIP_ID, "Dealer ID", null));
        keyData.add(createKeyDataPoint(Key.DEALERSHIP_NAME, "Dealer Name", 150));

        keyData.add(createKeyDataPoint(Key.VEHICLE_ID, "Vehicle ID", null));
        keyData.add(createKeyDataPoint(Key.VEHICLE_TYPE, "Type", null));
        keyData.add(createKeyDataPoint(Key.VEHICLE_MANUFACTURER, "Make", null));
        keyData.add(createKeyDataPoint(Key.VEHICLE_MODEL, "Model", null));

        keyData.add(createKeyDataPoint(Key.VEHICLE_PRICE, "Price", null));
        keyData.add(createKeyDataPoint(Key.VEHICLE_PRICE_UNIT, "Money Unit", null));

        keyData.add(createKeyDataPoint(Key.REASON_FOR_ERROR, "Issue", 500));

        return keyData;
    }
    
    public static JTable createTableFromMapList(List<Map<Key, Object>> data, List<Map<String, Object>> keyData) {
        if (data == null || data.isEmpty()) {return null;}

        // Use Key enum values for column names (maintains order)
        String[] columnNames = new String[keyData.size()];

        for (int i = 0; i < keyData.size(); i++) {
            columnNames[i] = (String) keyData.get(i).get("name");
        }

        // Prepare table data
        Object[][] tableData = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            Map<Key, Object> rowMap = data.get(i);
            for (int j = 0; j < keyData.size(); j++) {
                tableData[i][j] = rowMap.get( (Key) keyData.get(j).get("key") );
            }
        }

        // Create the JTable
        DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
        JTable jTable = new JTable(model);
        for (int i = 0; i < columnNames.length; i++) {
            TableColumnModel colModel = jTable.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            col.setPreferredWidth((int) keyData.get(i).get("width"));
        }

        return jTable;
    }

    /**
     * Creates a JTable from a List of Maps.
     *
     * @param data The List of Maps containing vehicle data.
     * @return A JTable displaying the vehicle data, or null if the input list is null or empty.
     */
    public static JTable createTableFromMapList(List<Map<Key, Object>> data) {
        if (data == null || data.isEmpty()) {return null;}

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
                Object rowObject = rowMap.get(keys[j]);
                if (keys[j].equals(Key.VEHICLE_ACQUISITION_DATE)) {
                    if (rowObject != null) {
                        rowObject = (new Date((Long) rowObject)).toString();
                    } else {
                        rowObject = "";
                    }
                }
                tableData[i][j] = rowObject;
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