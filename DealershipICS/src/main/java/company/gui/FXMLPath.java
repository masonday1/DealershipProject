package company.gui;

/**
 * A utility class containing constants for FXML file paths used in the application.
 * This class centralizes FXML path definitions to improve maintainability and reduce redundancy.
 */
enum FXMLPath {
    MAIN_SCREEN ("/MainScreen.fxml", "Main Menu"),
    INVENTORY_SCREEN ("/InventoryScreen.fxml", "Inventory Management"),
    PROFILE_MANAGEMENT ("/ProfileManagement.fxml", "Profile Management"),
    ADD_INVENTORY ("/AddInventory.fxml", "Add Inventory"),
    ADD_FROM_FILE ("/AddFromFile.fxml","Load From File"),
    VEHICLE_ENTRY ("/VehicleEntry.fxml", "Add Inventory Manually"),
    VEHIClE_RENTAL ("/VehicleRental.fxml", "Modify Vehicle Rental Status"),
    VEHICLE_REMOVAL("/VehicleRemoval.fxml", "Remove Vehicle From Dealership"),
    VIEW_INVENTORY ("/ViewInventory.fxml", "Company Inventory");
    // Add other FXML paths as needed

    private final String PATH;
    private final String SCREEN_TITLE;
    
    FXMLPath(String path, String screenTitle) {
        PATH = path;
        SCREEN_TITLE = screenTitle;
    }

    public String getPath() {return PATH;}
    public String getScreenTitle() {return SCREEN_TITLE;}
}
