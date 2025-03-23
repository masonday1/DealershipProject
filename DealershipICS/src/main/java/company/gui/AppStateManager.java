package company.gui;

import javafiles.Key;
import javafiles.domainfiles.Company;
import javafiles.domainfiles.Dealership;

import java.util.List;
import java.util.Map;


/**
 * Manages the application's state, specifically the Company instance and its data.
 * This class provides static methods to initialize, access, and modify the Company object,
 * as well as retrieve data related to the company's inventory and dealerships.
 */
public class AppStateManager {

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
}
