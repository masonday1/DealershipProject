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
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static company.gui.FXMLPaths.ADD_FROM_FILE;
import static company.gui.FXMLPaths.ADD_INVENTORY;

public class AddFromFile {

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

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_INVENTORY);
    }


    public void handleAnother(ActionEvent actionEvent) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance(null);
        sceneManager.switchScene(ADD_FROM_FILE);
    }
}
