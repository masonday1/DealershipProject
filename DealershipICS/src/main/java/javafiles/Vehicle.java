package javafiles;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
    private Long vehiclePrice;
    private Long acquisitionDate;
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
        this.vehiclePrice = vehiclePrice;
    }

    /**
     * Sets the acquisition date of the vehicle. If null, sets to max long.
     *
     * @param acquisitionDate the date the vehicle was acquired
     */
    public void setAcquisitionDate(Long acquisitionDate) {
        // TODO: This may need to be handled differently (account for null)
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
        // TODO: This (Date) needs to be handled differently (account for null)
        long tempAcquisitionDate;
        tempAcquisitionDate = Objects.requireNonNullElse(acquisitionDate, -1L);
        Date date = new Date(tempAcquisitionDate);
        return  "Vehicle: " +  vehicleType +
                "\nID: " + vehicleId +
                "\nManufacturer: " + vehicleManufacturer +
                "\nModel: " + vehicleModel +
                "\nPrice: $" + vehiclePrice +
                "\nAcquired: " + date;
    }

    private void putNonNull(Map<String, Object> map, String key, Object object) {
        if (object != null) {
            map.put(key, object);
        }
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
        putNonNull(map, Key.VEHICLE_TYPE.getKey(), vehicleType);
        putNonNull(map, Key.VEHICLE_MANUFACTURER.getKey(), vehicleManufacturer);
        putNonNull(map, Key.VEHICLE_MODEL.getKey(), vehicleModel);
        putNonNull(map, Key.VEHICLE_ID.getKey(), vehicleId);
        putNonNull(map, Key.VEHICLE_PRICE.getKey(), vehiclePrice);
        putNonNull(map, Key.VEHICLE_ACQUISITION_DATE.getKey(), acquisitionDate);
    }
}
