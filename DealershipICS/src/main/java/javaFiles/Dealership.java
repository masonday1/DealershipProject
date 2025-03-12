package javaFiles;

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


    // Method for adding new vehicles to the dealership.
    public void addIncomingVehicle(Vehicle newVehicle) {
        // Checks if the dealership is accepting new vehicles.
        if (!receivingVehicle) {
            System.out.println("Dealership " + this.dealerId + " is not accepting new vehicles at this time.");
            System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealerId +".");
            return; // Exits method if the dealership is not accepting new vehicles.
        } 
        
        // Checks if the new vehicle is already located at the dealership. 
        for (Vehicle vehicle : salesInventory) {
            if (vehicle.getVehicleId().equals(newVehicle.getVehicleId())) {
                System.out.println("This vehicle is already located at the dealership.");
                System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealerId + ".");
                return; // Exits method if the vehicle already exists at the dealership
            }
        }
        this.salesInventory.add(newVehicle);
    }

    /**
     * Creates a new Vehicle object based on the given type.
     * <p>
     * This method acts as a factory for creating different types of vehicles.  It uses
     * a switch statement to determine which concrete Vehicle class to instantiate
     * based on the provided argument vehicleType
     *
     * @param vehicleType The type of vehicle to create ("suv", "sedan", "pickup", "sports car").
     * @param ID          The ID of the vehicle. This is used in the error message if the
     *                    vehicle type is not supported.
     * @return A new {@link Vehicle} object of the specified type, or  null if
     *         the vehicleType is not supported. If null is returned, a
     *         message is printed to the console indicating the unsupported type and
     *         the vehicle ID was not added.
     */
    private static Vehicle createNewVehicle(String vehicleType, String ID) {
        return switch (vehicleType.toLowerCase()) {
            case "suv" -> new SUV();
            case "sedan" -> new Sedan();
            case "pickup" -> new Pickup();
            case "sports car" -> new SportsCar();
            default -> {
                System.out.println("\"" + vehicleType +
                        "\" is not a supported vehicle type. " +
                        "Vehicle ID: " + ID + "was not added");
                yield null;
            }
        };
    }

    /**
     * Takes a Map with information about a Vehicle, creates that Vehicle and adds to inventory.
     *
     * @param map The data needed to create the new Vehicle.
     */
    public void dataToInventory(Map<String, Object> map) {
        Vehicle vehicle = createNewVehicle(
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
     * using the provided details. It then attempts to add the new vehicle to the dealership's
     * inventory, but only if the vehicle type is valid. If the vehicle type is invalid, an error
     * message is printed, and the vehicle is not added.
     *</p>
     * {@link #createNewVehicle(String,String)} is used to create and validate the vehicle type.
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
     *
     */
    public void manualVehicleAdd(String vehicleID, String vehicleManufacturer, String vehicleModel, long vehiclePrice, long acquisitionDate, String vehicleType) {

        // Ensure the vehicle price is positive.
        if (vehiclePrice <= 0) {
            System.out.println("Error: Vehicle price must be a positive value. Vehicle ID: " + vehicleID + " was not added.");
            return;  // Exit the method if the price is not positive.
        }

        Vehicle newVehicle = createNewVehicle(vehicleType, vehicleID);

        // Check if the vehicle creation was successful (newVehicle is not null).
        if (newVehicle == null) {
            // Handle the case where the vehicle type is unsupported.
            System.out.println("Vehicle creation failed. Invalid vehicle type: " + vehicleType);
            return;  // Exit the method if vehicle creation failed.
        }


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