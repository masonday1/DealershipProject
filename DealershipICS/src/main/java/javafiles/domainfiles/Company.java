package javafiles.domainfiles;

import javafiles.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Company {
    private String companyId;
    private String companyName;
    private ArrayList<Dealership> listDealerships;


    public Company(String companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.listDealerships = new ArrayList<>();
    }

    public void addDealership(Dealership dealership) {listDealerships.add(dealership);}

    public ArrayList<Dealership> getListDealerships() {return listDealerships;}

    /**
     * Takes a String representing a Dealership ID and returns the index of that
     * Dealership in this Company's listDealerships.
     *
     * @param dealerId A String equal to the dealerId of the Dealership we are searching for.
     * @return The index of the searched for Dealership in listDealerships (-1 if absent).
     */
    public int getDealershipIndex(String dealerId) {
        Dealership dealership;
        for (int i = 0; i < listDealerships.size(); i++) {
            dealership = listDealerships.get(i);
            if (dealership.getDealerId().equals(dealerId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Takes a String representing a Dealership ID and returns the given
     * Dealership in this Company's listDealerships.
     *
     * @param dealerId A String equal to the dealerID of the Dealership we are searching for.
     * @return The Dealership we are searching for in listDealerships (null if absent).
     */
    private Dealership findDealership(String dealerId) {
        for (Dealership dealership : listDealerships) {
            if (dealership.getDealerId().equals(dealerId)) {
                return dealership;
            }
        }
        return null;
    }

    public String getCompanyId() {return companyId;}

    public String getCompanyName() {return companyName;}

    /**
     * Takes a List of Map<String, Object>s representing a List of Vehicle information
     * and writes the data in each map to the corresponding Dealership.
     *
     * @param data The List of Maps containing Vehicle information to be added to inventory.
     */
    public void dataToInventory(List<Map<String, Object>> data) {
        if (data == null) {return;}
        for (Map<String, Object> map: data) {
            Dealership dealership = findDealership(Key.DEALERSHIP_ID.getValString(map));
            if (dealership == null) {
                dealership = new Dealership(Key.DEALERSHIP_ID.getValString(map));
                addDealership(dealership);
            }
            dealership.dataToInventory(map);
        }
    }

    /**
     * Retrieves Vehicle data for all Dealerships within the Company.
     * <p>
     * This method gathers Vehicle information from all Dealerships associated with the
     * Company and compiles it into a single list of Maps. Each Map in the List
     * represents a Vehicle and contains its attributes.
     *
     * @return A {@link List} of {@link Map} Objects. Each {@link Map} represents a Vehicle
     *         and contains its attributes (dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs. Returns all vehicles from each
     *         Dealership in the Company. Returns an empty list if the Company has no Dealerships
     *         or if none of the Dealerships have any Vehicles.
     */
    public List<Map<String, Object>> getDataMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            list.addAll(dealership.getDataMap());
        }
        if (list.isEmpty()) {return null;}
        return list;
    }

    /**
     * Prints the inventory of Vehicles for each Dealership in the Company.
     * <p>
     * This method iterates through the List of Dealerships associated in the Company.
     * For each {@link Dealership} it retrieves the Vehicle inventory and prints
     * information about each {@link Vehicle}. If a Dealership has no inventory, a message
     * indicating this is printed. If the Company has no Dealerships, a message is
     * printed to the console.
     */
    public void printInventory() {
        // if company does not have any dealerships, print message and return to menu
        if(listDealerships.isEmpty())
        {
            System.out.println("There are currently no dealerships in the company");
            return;
        }

        for(Dealership dealership : listDealerships)
        {
            dealership.printInventory();
            System.out.println();
        }
    }

    /**
     * Generates a formatted list of Dealership IDs.
     * <p>
     * This method retrieves all Dealerships associated with the Company and
     * creates a String containing their IDs, separated by tabs. The IDs are arranged
     * with a maximum of 6 IDs per line. If the Company has no Dealerships,
     * the method returns a message indicating this.
     *
     * @return A string containing the formatted list of dealership IDs, or the
     *         message "No valid Dealerships." if the company has no dealerships.
     */
    public String getDealershipIdList() {
        StringBuilder output = new StringBuilder();
        int added = 0;
        int idPerLine = 6;
        for (Dealership dealership : listDealerships) {
            output.append(dealership.getDealerId()).append("\t");
            if (added % idPerLine == idPerLine - 1) {
                output.append("\n");
            }
            added++;
        }
        if (output.isEmpty()) {
            return "No valid Dealerships.";
        }
        return output.toString();
    }

    /**
     * Returns a String displaying the current receiving status of the
     * Dealership at index dealerIndex in listDealerships.
     *
     * @param dealerIndex The index of the dealership that is being evaluated
     * @return A String displaying the receiving status of the Dealership.
     */
    public String changeReceivingStatusIntroString(int dealerIndex) {
        Dealership dealer = listDealerships.get(dealerIndex);
        return "Enable or disable vehicle receiving status for dealership "
                + dealer.getDealerId() + "? (Enter 'enable' or 'disable')\n" +
                "Currently enabled? (" + dealer.getStatusAcquiringVehicle() + ")";
    }

    /**
     * Updates the Dealership receiving status for Vehicles and prints the appropriate
     * message for that update based on what the userInput read from the user.
     *
     * @param dealerIndex The index of the Dealership to be updated in listDealerships.
     * @param userInput The input provided by the user that is being processed.
     * @return Whether the input is invalid, true if it is invalid, false otherwise.
     */
    public boolean changeReceivingStatus(int dealerIndex, String userInput) {
        Dealership dealer = listDealerships.get(dealerIndex);
        if (userInput.equalsIgnoreCase("enable")) {
            // Check if the dealership's vehicle receiving status is already enabled
            if (dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to receive vehicles.");
            } else {
                // Enable vehicle receiving for the dealership
                dealer.enableReceivingVehicle();
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been enabled.");
            }
            return false;
        } else if (userInput.equalsIgnoreCase("disable")) {
            // Disable the vehicle receiving status
            if (!dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to not receive vehicles.");
            } else {
                dealer.disableReceivingVehicle();
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been disabled.");
            }
            return false;
        }
        System.out.println("Invalid input. Please enter 'enable' or 'disable'.");
        return true;
    }
}