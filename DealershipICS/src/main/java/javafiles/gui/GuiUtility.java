package javafiles.gui;


import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import javafiles.dataaccessfiles.FileIO;
import javafiles.dataaccessfiles.FileIOBuilder;
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
    public static void showMapFromFileInfo(List<Object[]> successData, List<Object[]> invalidData) {
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Type", "Dealership ID"};

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
    }

    public static void addFromFile(List<Map<Key, Object>> maps, List<Map<Key, Object>> badMaps ) {
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

            if (badMaps.contains(map)) {invalidData.add(rowData);}
            else {successData.add(rowData);}
        }

        showMapFromFileInfo(successData, invalidData);
    }
}