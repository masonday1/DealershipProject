package javaFiles;

import java.util.ArrayList;

/*  Creates Dealership object with the following attributes:
        Dealership ID (String)      (Required during instantiation)
        Vehicle acquisition status  (default: ENABLED)
        Vehicle inventory           (default: empty)

    Author: Patrick McLucas */

public class Dealership {
    private final String dealerID;
    private final ArrayList<Vehicle> vehicleInventory;
    private boolean receivingVehicle;

    // Instantiation requires dealer_ID
    public Dealership(String dealerID) {
        this.dealerID = dealerID;
        this.receivingVehicle = true;
        vehicleInventory = new ArrayList<>();
    }

    // Returns Dealer ID
    public String getDealerId () {
        return dealerID;
    }

    // Provides vehicle acquisition status
    public Boolean getStatusAcquiringVehicle() {
        return receivingVehicle;
    }

    // Provides list of vehicles at the dealership
    public ArrayList<Vehicle> getInventoryVehicles() {
        return vehicleInventory;
    }

    // ENABLES vehicle acquisition.
    public void enableReceivingVehicle() {
        this.receivingVehicle = true;
    }

    // DISABLES vehicle acquisition.
    public void disableReceivingVehicle() {
        this.receivingVehicle = false;
    }

    // Method for adding new vehicles to the dealership.
    public void addIncomingVehicle(Vehicle newVehicle) {
        // Checks if the dealership is accepting new vehicles.
        if (!receivingVehicle) {
            System.out.println("Dealership " + this.dealerID + " is not accepting new vehicles at this time.");
            System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealerID +".");
            return; // Exits method if the dealership is not accepting new vehicles.
        } 
        
        // Checks if the new vehicle is already located at the dealership. 
        for (Vehicle vehicle : vehicleInventory) {
            if (vehicle.getVehicleId().equals(newVehicle.getVehicleId())) {
                System.out.println("This vehicle is already located at the dealership.");
                System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealerID + ".");
                return; // Exits method if the vehicle already exists at the dealership
            }
        }
        this.vehicleInventory.add(newVehicle);
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
    public static Vehicle createNewVehicle(String vehicleType, String ID) {
        return switch (vehicleType) {
            case "suv" -> new SUV();
            case "sedan" -> new Sedan();
            case "pickup" -> new Pickup();
            case "sports car" -> new Sports_Car();
            default -> {
                System.out.println("\"" + vehicleType +
                        "\" is not a supported vehicle type. " +
                        "Vehicle ID: " + ID + "was not added");
                yield null;
            }
        };
    }



}


