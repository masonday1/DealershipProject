package javafiles.domainfiles;

import javafiles.customexceptions.*;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javafiles.dataaccessfiles.Key;

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
    private boolean rental;
    private final RentalStrategy rentalStrategy;

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type with default rental strategy
     *
     * @param vehicleType represents the specific vehicle type that the extending class is
     */
    public Vehicle(String vehicleType)
    {
        this.vehicleType = vehicleType;
        this.rental = false;
        this.rentalStrategy = new DefaultRentalStrategy();
    }

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type with  a specified rental strategy
     *
     *
     * @param vehicleType the specific vehicle type that the extending class is
     * @param rentalStrategy the rental strategy used for this vehicle
     */
    public Vehicle(String vehicleType, RentalStrategy rentalStrategy)
    {
        this.vehicleType = vehicleType;
        this.rentalStrategy = rentalStrategy;
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
     * Sets the vehicle price.
     *
     * @param vehiclePrice the price of the vehicle
     */
    public void setVehiclePrice(Long vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    /**
     * Sets the acquisition date of the vehicle.
     *
     * @param acquisitionDate the date the vehicle was acquired
     */
    public void setAcquisitionDate(Long acquisitionDate) { this.acquisitionDate = acquisitionDate;
    }


    /**
     * Sets the rental state of the vehicle object.
     *
     * @param state {@code true} to enable rentals, {@code false} to disable.
     */
    public void setRental(boolean state) {this.rental = state; }



    /**
     * Enables rentals using the configured rental strategy.
     *
     * @throws RentalException If an error occurs during the rental enabling process.
     */
    public void enableRental() throws RentalException
    {
        rentalStrategy.enableRental(this);

    }

    /**
     * Disables rentals using the configured rental strategy.
     *
     * @throws RentalException If an error occurs during the rental disabling process.
     */
    public void disableRental() throws RentalException
    {
        rentalStrategy.disableRental(this);
    }

    // Getter methods for shared attributes
    public String getVehicleId() {return vehicleId;}
    public String getVehicleManufacturer() {return vehicleManufacturer;}
    public String getVehicleModel() {return vehicleModel;}
    public Long getVehiclePrice() {return vehiclePrice;}
    public Long getAcquisitionDate() {return acquisitionDate;}
    public String getVehicleType() {return vehicleType;}
    public boolean getRentalStatus() { return rental; }

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
        tempAcquisitionDate = Objects.requireNonNullElse(acquisitionDate, Long.MAX_VALUE);

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