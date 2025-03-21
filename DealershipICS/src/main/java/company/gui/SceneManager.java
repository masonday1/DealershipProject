package company.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The SceneManager class is responsible for managing the scenes of the application.
 * It follows the Singleton design pattern to ensure that only one instance of SceneManager exists
 * for a given stage and that scenes can be loaded and switched.
 */
public class SceneManager {

    /** The stage for the application window. */
    private Stage stage;

    /** A map storing all the loaded scenes, keyed by their name. */
    private Map<String, Scene> scenes = new HashMap<>();

    /** The singleton instance of SceneManager. */
    private static SceneManager instance;

    /**
     * Private constructor to initialize the SceneManager with the given stage.
     * This constructor is private to ensure only one instance of SceneManager exists.
     *
     * @param stage the main stage (window) of the application
     */
    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the singleton instance of SceneManager.
     * If an instance does not exist, it creates one using the provided stage.
     *
     * @param stage the main stage (window) of the application
     * @return the singleton instance of SceneManager
     */
    public static SceneManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
        return instance;
    }

    /**
     * Loads a scene from the specified FXML file and stores it in the scenes map.
     * The scene is also set with a title based on the FXML file's name.
     *
     * @param name the name used to identify the scene in the scenes map
     * @param fxmlFile the path to the FXML file to load the scene from
     * @throws IOException if there is an error loading the FXML file
     */
    public void loadScene(String name, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scenes.put(name, scene);
        stage.setTitle(GuiUtility.getScreenTitle(fxmlFile));
    }

    /**
     * Switches to the scene identified by the given name.
     * If the scene is found in the scenes map, it sets the scene on the stage.
     *
     * @param name the name of the scene to switch to
     */
    public void switchScene(String name) {
        Scene scene = scenes.get(name);
        if (scene != null) {
            stage.setScene(scene);
        }
    }
}
