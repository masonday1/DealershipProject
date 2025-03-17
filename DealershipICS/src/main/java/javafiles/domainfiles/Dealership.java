package javafiles.domainfiles;

import javafiles.customexceptions.*;
import javafiles.dataaccessfiles.JSONIO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a dealership that manages vehicle sales and rentals.
 * <p>
 * This class provides functionality to add, remove, and retrieve vehicles from the dealership's
 * sales and rental inventories. It also allows for enabling and disabling vehicle acquisition
 * and rental services.
 * <p>
 * The dealership is identified by a unique dealer ID and maintains separate inventories for
 * vehicles available for sale and rental.
 * <p>
 * Authors: Patrick McLucas, Christopher Engelhart
 */

public class Dealership {
    private final String dealerId;
    private final ArrayList<Vehicle> salesInventory;
    private final ArrayList<Vehicle> rentalInventory;
    private static final VehicleFactory vehicleFactory = VehicleCreator.getInstance(); // Singleton
    private boolean receivingVehicle;
    private boolean rentalStatus;

    // Instantiation requires dealer_ID
    public Dealership(String dealerId) {
        this.dealerId = dealerId;
        this.receivingVehicle = true;
        this.rentalStatus = false;
        salesInventory = new ArrayList<>();
        rentalInventory = new ArrayList<>();
    }

    // Returns Dealer ID
    public String getDealerId () {
        return dealerId;
    }

    // Provides vehicle acquisition status
    public Boolean getStatusAcquiringVehicle() {
        return receivingVehicle;
    }

    public Boolean getRentalStatus() { return rentalStatus; }

    // Provides list of vehicles at the dealership
    public ArrayList<Vehicle> getSaleVehicles() {
        return salesInventory;
    }

    public ArrayList<Vehicle> getRentalVehicles() {
        return rentalInventory;
    }

    // ENABLES vehicle acquisition.
    public void enableReceivingVehicle() {
        this.receivingVehicle = true;
    }

    // DISABLES vehicle acquisition.
    public void disableReceivingVehicle() {
        this.receivingVehicle = false;
    }


    // enable dealership to perform rental services
    public void enableRentalService(){this.rentalStatus = true; }


    // disable dealership's rental services
    public void disableRentalService() {this.rentalStatus = false; }


    /**
     * Retrieves a vehicle from the sales inventory by its ID.
     *
     * @param vehicleID The ID of the vehicle to retrieve.
     * @return The Vehicle object.
     * @throws VehicleNotFoundException if the vehicle is not found.
     */
    public Vehicle getVehicleFromSalesInventory(String vehicleID) throws VehicleNotFoundException {
        for (Vehicle vehicle : salesInventory) {
            if (vehicle.getVehicleId()!= null && vehicle.getVehicleId().equals(vehicleID)) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with ID: " + vehicleID + " not found in sales inventory.");
    }

    /**
     * Retrieves a vehicle from the rental inventory by its ID.
     *
     * @param vehicleID The ID of the vehicle to retrieve.
     * @return The Vehicle object.
     * @throws VehicleNotFoundException if the vehicle is not found.
     */
    public Vehicle getVehicleFromRentalInventory(String vehicleID) throws VehicleNotFoundException {


        for (Vehicle vehicle : rentalInventory) {
            if (vehicle.getVehicleId()!= null && vehicle.getVehicleId().equals(vehicleID)) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with ID: " + vehicleID + " not found in rental inventory.");
    }



    /**
     * Checks if a vehicle is already present in the given inventory.
     *
     * @param newVehicle The vehicle to check for in the inventory.
     * @param inventory The inventory (list) where the vehicle might be located.
     * @return {@code true} if the vehicle is found in the inventory, {@code false} otherwise.
     *
     * @author Christopher Engelhart
     */
    private boolean isVehicleInInventory(Vehicle newVehicle, List<Vehicle> inventory)
    {
        for (Vehicle vehicle : inventory)
        {
            if (vehicle.getVehicleId().equals(newVehicle.getVehicleId()))
            {
                return true;
            }
        }

        return false;
    }



    /**
     * Adds a new vehicle to the dealership's sales inventory.
     * <p>
     * This method checks if the dealership is currently accepting new vehicles and if the vehicle is already
     * present in either the sales or rental inventory. If the dealership is not accepting new vehicles or if
     * the vehicle already exists, an exception is thrown.
     *
     * @param newVehicle The {@link Vehicle} object to be added to the inventory.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     */
    public void addIncomingVehicle(Vehicle newVehicle) throws DealershipNotAcceptingVehiclesException,
            VehicleAlreadyExistsException
    {
        // Checks if the dealership is accepting new vehicles.
        if (!receivingVehicle) {
            throw new DealershipNotAcceptingVehiclesException("Dealership " + this.dealerId + " is not accepting new " +
                    "vehicles at this time. " + "Vehicle ID: " + newVehicle.getVehicleId() +
                    " was not added to Dealership: " + this.dealerId + ".");
        } 

        if (isVehicleInInventory(newVehicle, salesInventory))
        {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the sales inventory. Vehicle ID: "
                    + newVehicle.getVehicleId() + " was not added to dealership " + this.dealerId + ".");
        }

        if (isVehicleInInventory(newVehicle, rentalInventory))
        {
            throw new VehicleAlreadyExistsException("This vehicle is already located in the rental inventory. Vehicle ID: "
                    + newVehicle.getVehicleId() + " was not added to dealership " + this.dealerId + ".");
        }


        this.salesInventory.add(newVehicle);
    }



    /**
     * Takes a Map with information about a Vehicle, creates that Vehicle and adds to inventory.
     *
     * @param map The data needed to create the new Vehicle.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     */
    public void dataToInventory(Map<String, Object> map) throws InvalidVehicleTypeException,
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException {

        Vehicle vehicle = vehicleFactory.createVehicle(
                JSONIO.getTypeVal(map),
                JSONIO.getVehicleIdVal(map)
        );
        if (vehicle == null) {
            return;
        }
        vehicle.setVehicleId(JSONIO.getVehicleIdVal(map));
        vehicle.setVehicleManufacturer(JSONIO.getManufacturerVal(map));
        vehicle.setVehicleModel(JSONIO.getModelVal(map));
        vehicle.setVehicleId(JSONIO.getVehicleIdVal(map));
        vehicle.setVehiclePrice(JSONIO.getPriceVal(map));
        vehicle.setAcquisitionDate(JSONIO.getDateVal(map));

        addIncomingVehicle(vehicle);
    }

    /**
     * Adds a new vehicle to the dealership inventory based on the provided vehicle details.
     * This method creates a new vehicle based on the vehicle type and sets its attributes
     * using the provided parameters.
     *</p>
     * {@link VehicleFactory#createVehicle(String, String)} is used to create and validate the vehicle type.
     * If the vehicle type is unsupported, the method will print an error message and return without
     * making any changes to the inventory. If the vehicle is created successfully, it will be added
     * to the dealership's inventory using the {@link #addIncomingVehicle(Vehicle)} method.
     *
     *
     * @param vehicleID The unique identifier for the vehicle.
     * @param vehicleManufacturer The manufacturer of the vehicle.
     * @param vehicleModel The model of the vehicle.
     * @param vehiclePrice The price of the vehicle. The price must be a positive value representing the
     *                     cost of the vehicle.
     * @param acquisitionDate The date when the vehicle was acquired by the dealership.
     *                        @note acquisitionDate is a long value representing milliseconds
     *                        since the epoch.
     * @param vehicleType The type of the vehicle. This should be one of the following types:
     *                    "suv", "sedan", "pickup", or "sports car". If an unsupported type is provided,
     *                    the method will not add the vehicle and will print an error message.
     *  @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     *  @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     *  @throws InvalidVehicleTypeException If the vehicle type is not supported.
     * @throws InvalidPriceException if the vehicle price is not a positive value
     *
     * @author Christopher Engelhart
     */
    public void manualVehicleAdd(String vehicleID, String vehicleManufacturer, String vehicleModel, long vehiclePrice,
                                 long acquisitionDate, String vehicleType) throws InvalidVehicleTypeException,
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException, InvalidPriceException {

        // Ensure the vehicle price is positive.
        if (vehiclePrice <= 0) {
            throw new InvalidPriceException("Error: Vehicle price must be a positive value. Vehicle ID: " + vehicleID + " was not added.");
        }

        Vehicle newVehicle = vehicleFactory.createVehicle(vehicleType, vehicleID);

        newVehicle.setVehicleManufacturer(vehicleManufacturer);
        newVehicle.setVehicleModel(vehicleModel);
        newVehicle.setVehiclePrice(vehiclePrice);
        newVehicle.setAcquisitionDate(acquisitionDate);


        this.addIncomingVehicle(newVehicle);
    }


    /**
     * Adds a vehicle to the dealership's rental inventory.
     *
     * @param rental The vehicle to add to the rental inventory. Cannot be null.
     *
     * @throws IllegalArgumentException If the rental parameter is null.
     * @throws VehicleAlreadyExistsException If the vehicle is already in the rental inventory.
     * @throws DealershipNotRentingException If the dealership does not currently provide rental services.
     * @throws VehicleNotRentableException If the vehicle is not currently rentable.
     *
     * @author Christopher Engelhart
     */
    public void addRentalVehicle(Vehicle rental) throws IllegalArgumentException,VehicleAlreadyExistsException,
            DealershipNotRentingException, VehicleNotRentableException {

        if (rental == null) {
            throw new IllegalArgumentException("Rental vehicle is null.");
        }

        if (!this.getRentalStatus()) {
            throw new DealershipNotRentingException("Dealership " + this.getDealerId() + " is not currently providing rental services.");
        }

        if (!rental.getRentalStatus()) {
            throw new VehicleNotRentableException("Vehicle " + rental.getVehicleId() + " is not currently rentable.");
        }

        if (this.isVehicleInInventory(rental,this.rentalInventory)) {
            throw new VehicleAlreadyExistsException("Vehicle " + rental.getVehicleId() + " is already in the rental inventory.");
        }

        this.rentalInventory.add(rental);
    }




    /**
     * Removes a vehicle from the inventory. Returns true if vehicle is removed and false otherwise.
     *
     * @param targetVehicle The vehicle to remove. Cannot be null.
     * @param inventory     The inventory from which to remove the vehicle.
     * @return {@code true} if the vehicle was successfully removed, {@code false} otherwise.
     * @throws IllegalArgumentException If the {@code targetVehicle} is null.
     * @throws EmptyInventoryException  If the inventory is empty.
     *
     * @author Christopher Engelhart
     */
    public boolean tryRemoveVehicleFromInventory(Vehicle targetVehicle, ArrayList<Vehicle> inventory) throws IllegalArgumentException
        ,EmptyInventoryException
    {

        if (targetVehicle == null) {
            throw new IllegalArgumentException("target vehicle is null.");
        }

        if (inventory.isEmpty())
        {
            throw new EmptyInventoryException("Inventory is already empty " + " Could not remove vehicle" + targetVehicle.getVehicleId());
        }

        return inventory.remove(targetVehicle);

    }



    /**
     * Retrieves Vehicle data for the Dealership.
     * <p>
     * This method generates a List of Maps, where each Map represents a Vehicle
     * in the specified Dealership's inventory. Each Map contains key-value pairs
     * representing the vehicle's attributes.
     *
     *@return {@link List} of {@link Map} Objects where each Map object holds a specific vehicle
     *         and its data.(dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs.
     */
    public List<Map<String, Object>> getDataMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Vehicle vehicle: salesInventory) {
            Map<String, Object> map = new HashMap<>();
            map.put(JSONIO.getDealIdKey(), dealerId);
            vehicle.getDataMap(map);
            list.add(map);
        }
        return list;
    }

    /**
     * Prints the inventory of Vehicles for the Dealership.
     * <p>
     * This method iterates through both the sales and rental inventory of vehicles in the Dealership.
     * It prints the Dealership ID followed by the inventory of Vehicles
     * separated by an empty line If a Dealership has no inventory,
     * a message indicating this is printed.
     */
    public void printInventory() {

        if (salesInventory.isEmpty() && rentalInventory.isEmpty()) {
            System.out.println("Dealership: " + dealerId);
            System.out.println("Sales does not currently have any inventory");
            System.out.println("Rental does not currently have any inventory\n");
            return;
        }

        System.out.println("Dealership: " + dealerId);
        System.out.println("----------------------");

        if (!salesInventory.isEmpty()) {
            System.out.println("Sales: \n");
            for (Vehicle vehicle : salesInventory) {
                System.out.println("\n" + vehicle.toString());
            }
        } else {
            System.out.println("Sales does not currently have any inventory\n");
        }

        if (!rentalInventory.isEmpty()) {
            System.out.println("\nRental: \n");
            for (Vehicle vehicle : rentalInventory) {
                System.out.println("\n" + vehicle.toString());
            }
        } else {
            System.out.println("\nRental does not currently have any inventory\n");
        }
    }

}