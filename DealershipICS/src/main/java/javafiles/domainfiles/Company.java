package javafiles.domainfiles;

import javafiles.Key;
import javafiles.customexceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a central manager for all dealerships within a vehicle dealership system.
 * <p>
 * This class maintains a list of Dealership objects and provides functionality to add and retrieve dealerships by ID or name.
 * It supports operations for toggling rental status for vehicles, enabling or disabling vehicle receiving
 * at dealerships, and transferring vehicles between rental and sales inventories. The class also handles
 * data import by mapping structured input into dealership inventories.
 * </p>
 */
public class Company {
    private final ArrayList<Dealership> listDealerships;

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
     * @param dealerId A String equal to the dealerID of the target Dealership.
     * @return The Dealership target dealership (null if absent).
     */
    public Dealership findDealership(String dealerId)  {
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
    public boolean isDealershipRentingEnabled(String dealershipId){
        Dealership dealership = findDealership(dealershipId);
        if (dealership != null) {
            return dealership.getRentingVehicles();
        }
        return false; // Dealership not found, or renting is disabled.
    }

    /**
     * Gets the complete inventory of a target dealership in company object.
     * Method calls {@link Dealership#getTotalInventory()}.
     *
     * @param dealershipId dealership ID of target dealership
     * @return a total collection of target dealership's sales and rental inventory
     */
    public ArrayList <Vehicle> getDealershipCompleteInventory(String dealershipId){
        Dealership dealership = findDealership(dealershipId);
        return dealership.getTotalInventory();
    }


    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     *
     * @param dealershipid The ID of the dealership containing the vehicle to update.
     * @param vehicle       The vehicle object with the updated rental status. This is the same vehicle object that
     * is present in the dealership's inventory (either sales or rental).
     * @throws RentalException       If the vehicle is a sports car, which is not rentable.
     */
    public void updateVehicleRental(String dealershipid, Vehicle vehicle) throws RentalException
    {
        Dealership dealership = findDealership(dealershipid);

        // Update the vehicle's rental status
        if (!vehicle.getVehicleType().equalsIgnoreCase("Sports car")) {
            if(vehicle.getRentalStatus())
            {
                vehicle.disableRental();
            }
            else
            {
                vehicle.enableRental();
            }
        }

        else {
            throw new VehicleNotRentableException("Sports car types are not currently rentable");
        }

        // Remove from the source inventory and add vehicle to opposite inventory
        if (dealership.getSaleVehicles().contains(vehicle)) {
            dealership.getSaleVehicles().remove(vehicle);
            dealership.getRentalVehicles().add(vehicle);
        } else {
            dealership.getRentalVehicles().remove(vehicle);
            dealership.getSaleVehicles().add(vehicle);
        }
    }

    public void manualVehicleAdd(Map<Key, Object> map, Dealership dealer) throws InvalidVehicleTypeException,
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException, InvalidPriceException {
        String id = Key.VEHICLE_ID.getVal(map, String.class);
        String make = Key.VEHICLE_MANUFACTURER.getVal(map, String.class);
        String model = Key.VEHICLE_MODEL.getVal(map, String.class);
        Long price = Key.VEHICLE_PRICE.getVal(map, Long.class);
        Long acqDate = Key.VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
        String type = Key.VEHICLE_TYPE.getVal(map, String.class);
        String unit = Key.VEHICLE_PRICE_UNIT.getVal(map, String.class);
        if (isVehicleInInventoryById(id)) {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the inventory. " +
                    "Vehicle ID: " + id + " was not added to dealership " + dealer.getDealerId() + ".");
        }
        dealer.manualVehicleAdd(id, make, model, price, acqDate, type, unit);
    }

    /**
     * Removes target {@link Vehicle} from a {@link Dealership} inventory.
     * Method calls {@link Dealership#removeVehicleFromInventory(Vehicle)}.
     *
     * @param dealershipId target dealership to remove vehicle from
     * @param targetVehicle vehicle to be removed
     * @throws IllegalArgumentException if target vehicle is null
     */
    public void removeVehicleFromDealership(String dealershipId,Vehicle targetVehicle) throws  IllegalArgumentException{
        Dealership dealership = this.findDealership(dealershipId);
        dealership.removeVehicleFromInventory(targetVehicle);
    }

    /**
     * Transfers a vehicle from one dealership's inventory to another.
     * </p>
     * Calls the {@link Dealership#addIncomingVehicle(Vehicle)} method
     * to add the transfer vehicle to the receving dealership.
     *
     * @param senderId        The ID of the dealership sending the vehicle.
     * @param receiverId      The ID of the dealership receiving the vehicle.
     * @param transferVehicle The vehicle to be transferred.
     * @throws DuplicateSenderException         If the sender and receiver dealership IDs are the same.
     * @throws VehicleAlreadyExistsException    If the receiving dealership already has the vehicle in its inventory.
     * @throws DealershipNotAcceptingVehiclesException If the receiving dealership is not accepting vehicles.
     */
    public void dealershipVehicleTransfer(String senderId, String receiverId, Vehicle transferVehicle)
            throws DuplicateSenderException, VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException
    {
        Dealership senderDealer = this.findDealership(senderId);
        Dealership receivingDealer = this.findDealership(receiverId);

        if (senderId.equals(receiverId))
        {
            throw new DuplicateSenderException("Sender and receiver dealership can not be the same");
        }

        senderDealer.removeVehicleFromInventory(transferVehicle);
        receivingDealer.addIncomingVehicle(transferVehicle);
    }

    private boolean isVehicleInInventoryById(String id){
        for (Dealership dealership : listDealerships) {
            if (dealership.isVehicleInInventoryById(id)) {
                return true;
            }
        }
        return false;
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
                MissingCriticalInfoException cause = new MissingCriticalInfoException("No dealerID.");
                ReadWriteException exception = new ReadWriteException(cause);
                Key.REASON_FOR_ERROR.putNonNull(map, exception);
                badInventoryMaps.add(map);
                continue;
            }

            if (isVehicleInInventoryById(Key.VEHICLE_ID.getVal(map, String.class))) {
                DuplicateKeyException cause = new DuplicateKeyException("Duplicate Vehicle ID in inventory");
                ReadWriteException exception = new ReadWriteException(cause);
                Key.REASON_FOR_ERROR.putNonNull(map, exception);
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