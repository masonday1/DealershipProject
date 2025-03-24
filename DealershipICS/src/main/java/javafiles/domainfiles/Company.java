package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {
    private ArrayList<Dealership> listDealerships;

    public Company() {
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
    public Dealership findDealership(String dealerId) {
        for (Dealership dealership : listDealerships) {
            if (dealership.getDealerId().equals(dealerId)) {
                return dealership;
            }
        }
        return null;
    }


    /**
     * Checks if a dealership with the given ID has renting services enabled.
     *
     * @param dealershipId The ID of the dealership to check.
     * @return true if the dealership has renting enabled, false otherwise.
     */
    public boolean isDealershipRentingEnabled(String dealershipId) {
        Dealership dealership = findDealership(dealershipId);
        if (dealership != null) {
            return dealership.getRentingVehicles();
        }
        return false; // Dealership not found, or renting is disabled.
    }



    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     *
     * @param dealershipid     The dealershipid of {@link Dealership }containing the vehicle to update.
     * @param updatedVehicle The {@link Vehicle} with the updated rental status.
     * @throws VehicleAlreadyExistsException       If the vehicle already exists in the destination inventory.
     * @throws DealershipNotRentingException       If the dealership is not currently renting vehicles.
     * @throws VehicleNotRentableException         If the updated vehicle is not rentable.
     * @throws EmptyInventoryException            If the source inventory is empty.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not accepting new vehicles.
     * @throws VehicleNotFoundException            if the vehicle is not found in inventory
     */
    public void updateVehicleRental(String dealershipid, Vehicle updatedVehicle)
            throws VehicleAlreadyExistsException, DealershipNotRentingException,
            VehicleNotRentableException, EmptyInventoryException,
            DealershipNotAcceptingVehiclesException {

        Vehicle foundVehicle = null;
        ArrayList<Vehicle> sourceInventory = null;
        Dealership dealership = findDealership(dealershipid);

        // Find the vehicle in either inventory
        for (Vehicle vehicle : dealership.getSaleVehicles()) {
            if (vehicle.getVehicleId().equals(updatedVehicle.getVehicleId())) {
                foundVehicle = vehicle;
                sourceInventory = dealership.getSaleVehicles();
                break;
            }
        }
        if (foundVehicle == null) {
            for (Vehicle vehicle : dealership.getRentalVehicles()) {
                if (vehicle.getVehicleId().equals(updatedVehicle.getVehicleId())) {
                    foundVehicle = vehicle;
                    sourceInventory = dealership.getRentalVehicles();
                    break;
                }
            }
        }

        if (foundVehicle == null) {
            // Vehicle not found in either inventory
            throw new VehicleNotFoundException("Vehicle not found in dealershipID " + dealership.getDealerId() + " inventory");
        }

        // Update the vehicle's rental status
        foundVehicle.setRental(updatedVehicle.getRentalStatus());

        // Move the vehicle to the appropriate inventory
        if (updatedVehicle.getRentalStatus()) {
            // Move to rental inventory
            dealership.addRentalVehicle(foundVehicle);
        } else {
            // Move to sales inventory
            dealership.addIncomingVehicle(foundVehicle);
        }

        // Remove from the source inventory
        dealership.tryRemoveVehicleFromInventory(foundVehicle, sourceInventory);
    }



    /**
     * Takes a List of Map<Key, Object>s representing a List of Vehicle information
     * and writes the data in each map to the corresponding Dealership.
     *
     * @param data The List of Maps containing Vehicle information to be added to inventory.
     */
    public List<Map<Key, Object>> dataToInventory(List<Map<Key, Object>> data) {
        List<Map<Key, Object>> badInventoryMaps = new ArrayList<>();

        if (data == null) {return badInventoryMaps;}

        // Used to ensure that the cars for new Dealerships are
        // added before considering rental or receiving statuses.
        // Assumes that all Vehicles from the same dealership have
        // the same rental and receiving statuses.
        Map<Dealership, Map<Key, Object>> newDealershipStat = new HashMap<>();

        for (Map<Key, Object> map: data) {
            if (map.containsKey(Key.REASON_FOR_ERROR)) {
                badInventoryMaps.add(map);
                continue;
            }

            String id = Key.DEALERSHIP_ID.getVal(map, String.class);
            String name = Key.DEALERSHIP_NAME.getVal(map, String.class);

            if (id == null) {
                Key.REASON_FOR_ERROR.putNonNull(map, "No dealerID.");
                badInventoryMaps.add(map);
                continue;
            }

            Dealership dealership = findDealership(id);
            if (dealership == null) {
                dealership = new Dealership(id, name);
                addDealership(dealership);

                newDealershipStat.put(dealership, map);
            }
            if ( !dealership.dataToInventory(map) ) {
                badInventoryMaps.add(map);
            }
        }

        for (Dealership dealership : newDealershipStat.keySet()) {
            Map<Key, Object> map = newDealershipStat.get(dealership);
            dealership.setReceivingVehicle(Key.DEALERSHIP_RECEIVING_STATUS.getVal(map, Boolean.class));
            dealership.setRentingVehicles(Key.DEALERSHIP_RENTING_STATUS.getVal(map, Boolean.class));
        }

        return badInventoryMaps;
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
    public List<Map<Key, Object>> getDataMap() {
        List<Map<Key, Object>> list = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            list.addAll(dealership.getDataMap());
        }
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
            System.out.println(dealership.toFullString());
        }
    }

    //TODO: Decide whether we need this method still called in javafiles.Main
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
     * Returns an ArrayList of Strings representing all Dealership IDs in the company.
     *
     * @return An ArrayList of Strings containing all Dealership IDs.
     */
    public ArrayList<String> getAllDealershipIds() {
        ArrayList<String> dealershipIds = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            dealershipIds.add(dealership.getDealerId());
        }
        return dealershipIds;
    }

    public List<Map<String, Object>> getDealershipInfoList() {
        List<Map<String, Object>> dealershipInfoList = new ArrayList<>();
        for (Dealership dealership : listDealerships) {
            Map<String, Object> dealershipInfo = new HashMap<>();
            dealershipInfo.put("id", dealership.getDealerId());
            dealershipInfo.put("name", dealership.getDealerName());
            dealershipInfo.put("receivingEnabled", dealership.getStatusAcquiringVehicle());
            dealershipInfo.put("rentingEnabled", dealership.getRentingVehicles());
            dealershipInfoList.add(dealershipInfo);
        }
        return dealershipInfoList;
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
                dealer.setReceivingVehicle(true);
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been enabled.");
            }
            return false;
        } else if (userInput.equalsIgnoreCase("disable")) {
            // Disable the vehicle receiving status
            if (!dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to not receive vehicles.");
            } else {
                dealer.setReceivingVehicle(false);
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been disabled.");
            }
            return false;
        }
        System.out.println("Invalid input. Please enter 'enable' or 'disable'.");
        return true;
    }
}