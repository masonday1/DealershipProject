package company.gui;

import javafiles.Key;
import javafiles.customexceptions.*;
import javafiles.dataaccessfiles.FileIO;
import javafiles.dataaccessfiles.FileIOBuilder;
import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;
import javafiles.domainfiles.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;


/**
 * Manages the application's state, specifically the Company instance and its data.
 * This class provides static methods to initialize, access, and modify the Company object,
 * as well as retrieve data related to the company's inventory and dealerships.
 */
public class AppStateManager {

    private static String masterInventoryList = "masterInventoryList.json";
    private static Company company;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppStateManager() {}

    /**
     * Initializes the Company instance with the provided Company object.
     * If the Company instance already exists, this method will not overwrite it.
     *
     * @param newCompany The Company object to initialize the application state with.
     */
    public static void initializeCompany(Company newCompany)
    {
        if (company == null)
        {
            company = newCompany;
        }
    }

    /**
     * Retrieves the current Company instance.
     *
     * @return The Company object representing the application's state.
     */
    public static Company getCompany()
    {
        return company;
    }

    /**
     * Retrieves a List of Maps representing all vehicle data within the Company instance.
     * </p>
     * Each Map contains key-value pairs representing vehicle attributes.
     * Method calls {@link Company#getDataMap()}.
     *
     * @return A List of Maps containing vehicle data.
     */
    public static List<Map<Key, Object>> getCompanyData()
    {
        return company.getDataMap();
    }

    /**
     * Retrieves a List of Dealership objects associated with the Company instance.
     * </p>
     * Method calls {@link Company#getListDealerships()}.
     *
     * @return A List of Dealership objects.
     */
    public static List getListDealerships()
    {
        return company.getListDealerships();
    }


    /**
     * Adds a Dealership object to the Company instance.
     * </p>
     * Method calls {@link Company#addDealership(Dealership)}
     * 
     *
     * @param dealership The Dealership object to add to the Company.
     */
    public static void addADealership(Dealership dealership)
    {
        company.addDealership(dealership);
    }

    public static List<Map<Key, Object>> dataToInventory(List<Map<Key, Object>> maps) {
        return company.dataToInventory(maps);
    }

    protected static List<Map<Key, Object>> loadInitialFiles() {
        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(masterInventoryList, 'r');
            return company.dataToInventory(fileIO.readInventory());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    protected static void writeToInventory() {
        List<Map<Key, Object>> data = company.getDataMap();
        try {
            FileIO fileIO = FileIOBuilder.buildNewFileIO(masterInventoryList, 'w');
            fileIO.writeInventory(data);
        } catch (ReadWriteException e) {
            System.out.println("Inventory could not be loaded.");
        }
    }

    public static ArrayList<String> getDealershipIDs()
    {
        return company.getAllDealershipIds();
    }


    /**
     * Retrieves a list of dealership IDs that currently have renting enabled.
     *
     * @return A list of dealership IDs that are currently renting enabled.
     */
    public static List<String> getRentingEnabledDealershipIDs()
    {
        return company.getListDealerships().stream()
                .filter(dealership -> company.isDealershipRentingEnabled(dealership.getDealerId()))
                .map(Dealership::getDealerId)
                .collect(Collectors.toList());
    }




    /**
     * Retrieves a List of DealershipRow objects representing dealership data.
     * </p>
     * This method fetches dealership information from the Company instance using
     * {@link Company#getDealershipInfoList()} and converts it into a List of
     * {@link ProfileManagementController.DealershipRow} objects.
     *
     * @return A List of {@link ProfileManagementController.DealershipRow} objects containing
     *         dealership data such as ID, name, receiving status, and renting status.
     */
    public static List<ProfileManagementController.DealershipRow> getDealershipRows() {
        List<Map<String, Object>> dealershipInfoList = company.getDealershipInfoList();
        List<ProfileManagementController.DealershipRow> dealershipRows = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();

        for (Map<String, Object> info : dealershipInfoList) {
            String id = (String) info.get("id");
            String name = (String) info.get("name");
            Boolean receivingEnabled = (Boolean) info.get("receivingEnabled");
            Boolean rentingEnabled = (Boolean) info.get("rentingEnabled");

            // Check if the ID already exists
            if (!existingIds.contains(id)) {
                dealershipRows.add(new ProfileManagementController.DealershipRow(id, name, receivingEnabled, rentingEnabled));
                existingIds.add(id);
            } 
        }
        return dealershipRows;
    }




    /**
     * Manually adds a vehicle to a dealership's inventory.
     * This method locates the specified dealership, validates the vehicle data,
     * and adds the vehicle to the dealership's inventory.
     *
     * @param dealershipID      The ID of the dealership to add the vehicle to.
     * @param vehicleID         The unique ID of the vehicle.
     * @param vehicleManufacturer The manufacturer of the vehicle.
     * @param vehicleModel      The model of the vehicle.
     * @param vehiclePrice      The price of the vehicle.
     * @param acquisitionDate   The acquisition date of the vehicle.
     * @param vehicleType       The type of the vehicle.
     * @param priceUnit         The unit of the price.
     * @throws VehicleAlreadyExistsException       If a vehicle with the same ID already exists in the dealership's inventory.
     * @throws InvalidPriceException              If the vehicle price is invalid.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not accepting vehicles.
     * @throws InvalidVehicleTypeException         If the vehicle type is invalid.
     * @throws IllegalArgumentException            If the dealership ID is not found.
     */
    public static void manualVehicleAdd(String dealershipID, String vehicleID, String vehicleManufacturer, String vehicleModel, Long vehiclePrice, Long acquisitionDate, String vehicleType, String priceUnit)
            throws VehicleAlreadyExistsException, InvalidPriceException, DealershipNotAcceptingVehiclesException,
            InvalidVehicleTypeException {

        Dealership dealership = company.findDealership(dealershipID);
        if (dealership == null) {
            throw new IllegalArgumentException("Dealership ID not found: " + dealershipID);
        }

        dealership.manualVehicleAdd(vehicleID, vehicleManufacturer, vehicleModel, vehiclePrice, acquisitionDate, vehicleType,priceUnit);
    }


    /**
        Sets receiving status for a {@link Dealership} in the company.
        Method calls {@link Dealership#setReceivingVehicle(Boolean)}
     */
    public static void setDealershipReceivingStatus(Dealership dealership,boolean status)
    {
        dealership.setReceivingVehicle(status);
    }

    /**
     Sets rental status for a {@link Dealership} in the company.
     Method calls {@link Dealership#setRentingVehicles(Boolean)}
     */
    public static void setDealershipRentalStatus(Dealership dealership,boolean status)
    {
        dealership.setRentingVehicles(status);
    }

    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     * </p>
     * 
     * mwethod calls {@link Company#updateVehicleRental(String, Vehicle)}
     *
    */
    public static void updateDealershipVehicleRentalState(String dealershipid, Vehicle updatedVehicle) throws
            VehicleAlreadyExistsException, DealershipNotRentingException, VehicleNotRentableException,
            DealershipNotAcceptingVehiclesException, EmptyInventoryException
    {
        company.updateVehicleRental(dealershipid, updatedVehicle);
    }


    /**
     * Takes a String representing a Dealership ID and returns {@link Dealership}
     * with matching ID in the company.
     * </p>
     * Method calls {@link Company#findDealership(String)}.
     *
     * @param dealerId A String equal to the dealerID of the Dealership we are searching for.
     * @return The Dealership we are searching for in listDealerships (null if absent).
     */
    public static Dealership findADealership(String dealerId)
    {
       return company.findDealership(dealerId);
    }



    /**
     * Gets the complete inventory of a given dealership.
     *
     * @param dealershipId dealership ID of target dealership
     * @return ArrayList<Vehicle> represent a complete collection of target dealership's sales and rental inventory
     */
    public static ArrayList<Vehicle> getDealershipCompleteInventory(String dealershipId)
    {
        return company.getDealershipCompleteInventory(dealershipId);
    }

}
