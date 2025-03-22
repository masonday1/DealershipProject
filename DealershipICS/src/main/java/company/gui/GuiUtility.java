package company.gui;


import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * A utility class that contains commonly used functions for managing GUI elements,
 * such as navigating between screens and setting the window size.
 */
public class GuiUtility {

    /**
     * Helper method to get the screen title based on the FXML path.
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
            default:
                return "Unknown Screen";
        }
    }




    /**
     * Sets the size of the stage and ensures the size is retained across scene changes.
     * This method calculates the window size as a percentage of the screen size, and
     * allows you to keep that size even after scene transitions.
     *
     * @param stage The stage whose size is to be set.
     * @param widthPercentage The percentage of the screen width to be used for the stage's width.
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



}
