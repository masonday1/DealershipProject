package javafiles.gui;


import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
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
 * specifically for creating and manipulating JTables to display data.
 */

public class GuiUtility {



    /**
     * Creates a Map representing a key data point for JTable column configuration.
     *
     * @param key   The Key enum representing the data key.
     * @param name  The display name for the column header.
     * @param width The preferred width of the column (can be null for default width).
     * @return A Map containing the key data point.
     */
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

    /**
     * Creates a List of Maps representing key data for JTable column configuration.
     *
     * @return A List of Maps containing key data for JTable columns.
     */
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


    /**
     * Creates a JTable from a List of Maps using specified key data for column configuration.
     *
     * @param data    The List of Maps containing data for the JTable.
     * @param keyData The List of Maps containing key data for JTable columns.
     * @return A JTable displaying the data, or null if the input list is null or empty.
     */
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
                Key key = (Key) keyData.get(j).get("key");
                Object dataPoint = rowMap.get(key);
                if (key.equals(Key.REASON_FOR_ERROR)) {
                    Throwable throwable = key.getVal(rowMap, ReadWriteException.class);
                    Throwable finalThrowable = null;
                    int limit = 10;
                    while (throwable != null && limit > 0) {
                        finalThrowable = throwable;
                        throwable = throwable.getCause();
                        limit--;
                    }
                    if (finalThrowable != null) {
                        dataPoint = finalThrowable.getMessage();
                    }
                }
                tableData[i][j] = dataPoint;
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