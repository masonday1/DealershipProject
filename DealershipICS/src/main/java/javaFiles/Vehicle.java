package javaFiles;

import java.util.Date;
import java.util.Map;

/**
 Vehicle is an abstract class that defines a set of common attributes
 and behaviors for all vehicle types. This class serves as a blueprint for any
 specific vehicle types that may extend it

 @author Christopher Engelhart
 */


public abstract class Vehicle {
    private String vehicleId;
    private String vehicleManufacturer;
    private String vehicleModel;
    private long vehiclePrice;
    private long acquisitionDate;
    private final String vehicleType; // Common field to all vehicle

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type for example, SUV, Sedan, Pickup, Sports car.
     *
     * @param vehicleType represents the specific vehicle type that the extending class is
     */
    public Vehicle(String vehicleType)
    {
        this.vehicleType = vehicleType;

    }

    /**
     * Sets the vehicle ID.
     *
     * @param vehicleId the unique identifier for the vehicle
     */
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Sets the vehicle manufacturer.
     *
     * @param vehicleManufacturer the name of the vehicle's manufacturer
     */
    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    /**
     * Sets the vehicle model.
     *
     * @param vehicleModel the model name or number of the vehicle
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * Sets the vehicle price. If null, sets to max long.
     *
     * @param vehiclePrice the price of the vehicle
     */
    public void setVehiclePrice(Long vehiclePrice) {
        if (vehiclePrice == null) {
            this.vehiclePrice = Long.MAX_VALUE;
            return;
        }
        this.vehiclePrice = vehiclePrice;
    }

    /**
     * Sets the acquisition date of the vehicle. If null, sets to max long.
     *
     * @param acquisitionDate the date the vehicle was acquired
     */
    public void setAcquisitionDate(Long acquisitionDate) {
        if (acquisitionDate == null) {
            this.acquisitionDate = Long.MAX_VALUE;
            return;
        }
        this.acquisitionDate = acquisitionDate;
    }

    // Getter methods for shared attributes
    public String getVehicleId() {return vehicleId;}
    public String getVehicleManufacturer() {return vehicleManufacturer;}
    public String getVehicleModel() {return vehicleModel;}
    public long getVehiclePrice() {return vehiclePrice;}
    public long getAcquisitionDate() {return acquisitionDate;}
    public String getVehicleType() {return vehicleType;}

    /**
     * Creates and returns a String representation of the Vehicle
     *
     * @return A String representation of the Vehicle
     *
     * @author Dylan Browne
     */
    public String toString() {
        Date date = new Date(acquisitionDate);
        return  "Vehicle: " +  vehicleType +
                "\nID: " + vehicleId +
                "\nManufacturer: " + vehicleManufacturer +
                "\nModel: " + vehicleModel +
                "\nPrice: $" + vehiclePrice +
                "\nAcquired: " + date;
    }

    /**
     * Retrieves Vehicle data for a given Dealership.
     * <p>
     * This method fills a Map where each key-value pairs
     * represents the vehicle's attributes.
     *
     * @param map The Map to be filled with data from the Vehicle
     */
    public void getDataMap(Map<String, Object> map) {
        map.put(Key.VEHICLE_TYPE.getKey(), vehicleType);
        map.put(Key.VEHICLE_MANUFACTURER.getKey(), vehicleManufacturer);
        map.put(Key.VEHICLE_MODEL.getKey(), vehicleModel);
        map.put(Key.VEHICLE_ID.getKey(), vehicleId);
        map.put(Key.VEHICLE_PRICE.getKey(), vehiclePrice);
        map.put(Key.VEHICLE_ACQUISITION_DATE.getKey(), acquisitionDate);
    }
}
