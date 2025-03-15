package javaFiles;

import javaFiles.CustomExceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*  Creates Dealership object with the following attributes:
        Dealership ID (String)      (Required during instantiation)
        Vehicle acquisition status  (default: ENABLED)
        Vehicle inventory           (default: empty)

    Author: Patrick McLucas */

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
     * Checks if a vehicle is already present in the given inventory.
     *
     * @param newVehicle The vehicle to check for in the inventory.
     * @param inventory The inventory (list) where the vehicle might be located.
     * @return {@code true} if the vehicle is found in the inventory, {@code false} otherwise.
     *
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
     */
    public void manualVehicleAdd(String vehicleID, String vehicleManufacturer, String vehicleModel, long vehiclePrice,
                                 long acquisitionDate, String vehicleType) throws InvalidVehicleTypeException,
            VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException, InvalidPriceException {

        // Ensure the vehicle price is positive.
        if (vehiclePrice <= 0) {
            throw new InvalidPriceException("Error: Vehicle price must be a positive value. Vehicle ID: " + vehicleID + " was not added.");
        }

        Vehicle newVehicle = vehicleFactory.createVehicle(vehicleType, vehicleID);

        newVehicle.setVehicleId(vehicleID);
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
     * @throws Exception If an error occurs during the addition process, including:
     * - If the rental parameter is null.
     * - If the vehicle is already in the rental inventory.
     * - If the dealership does not currently provide rental services.
     * - If the vehicle is not currently rentable.
     *
     * @author Christopher Engelhart
     */
    public void addRentalVehicle(Vehicle rental) throws Exception {
        if (rental == null) {
            throw new Exception("Rental vehicle object is null.");
        }

        if (this.getRentalStatus() && rental.getRentalStatus()) {
            if (!this.rentalInventory.contains(rental)) {
                this.rentalInventory.add(rental);
            } else {
                throw new Exception("Vehicle " + rental.getVehicleId() + " is already in the rental inventory.");
            }
        } else {
            if (!this.getRentalStatus()) {
                throw new Exception("Dealership " + this.getDealerId() + " is not currently providing rental services.");
            } else if (!rental.getRentalStatus()) {
                throw new Exception("Vehicle " + rental.getVehicleId() + " is not currently rentable.");
            }
        }
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
     * This method iterates through the List of Vehicles in the Dealership.
     * It prints the Dealership ID followed by the inventory of Vehicles
     * separated by an empty line If a Dealership has no inventory,
     * a message indicating this is printed.
     */
    public void printInventory() {
        System.out.println("Dealership: " + dealerId);

        // if Dealership does not have any Vehicles, print message and return
        if (salesInventory.isEmpty()) {
            System.out.println(", Does not currently have any inventory\n");
            return;
        }

        for (Vehicle vehicle : salesInventory) {
            System.out.println("\n" + vehicle.toString());
        }
    }
}