package javafiles.domainfiles;

import javafiles.customexceptions.*;
import java.util.Date;
import java.util.Map;

import javafiles.Key;

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
    private String priceUnit;
    private Long acquisitionDate;
    private final String vehicleType; // Common field to all vehicle
    private boolean rental;
    private final RentalStrategy rentalStrategy;

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type with default rental strategy
     *
     * @param type the specific vehicle type that the extending class is
     * @param id The vehicle ID of the Vehicle
     */
    public Vehicle(String type, String id, String model, Long price) {
        this(type, id, model, price, new DefaultRentalStrategy());
    }

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type with  a specified rental strategy
     *
     *
     * @param type the specific vehicle type that the extending class is
     * @param id The vehicle ID of the Vehicle
     * @param strategy the rental strategy used for this vehicle
     */
    public Vehicle(String type, String id, String model, Long price, RentalStrategy strategy) {
        // necessary
        vehicleType = type;
        vehicleId = id;
        vehicleModel = model;
        vehiclePrice = price;
        rentalStrategy = strategy;

        // defaults
        vehicleManufacturer = "Unknown";
        rental = false;
        priceUnit = "dollars";
        acquisitionDate = null;
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
    public void setRental(boolean state) {this.rental = state;}

    public void setPriceUnit(String unit) {priceUnit = unit;}

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
    public String getPriceUnit() {return priceUnit;}

    /**
     * Creates and returns a String representation of the Vehicle
     *
     * @return A String representation of the Vehicle
     *
     * @author Dylan Browne
     */
    public String toString() {
        String dateStr = "Unknown";
        if (acquisitionDate != null) {
            dateStr = new Date(acquisitionDate).toString();
        }

        return  "Vehicle: " +  vehicleType +
                "\nID: " + vehicleId +
                "\nModel: " + vehicleModel +
                "\nManufacturer: " + vehicleManufacturer +
                "\nPrice: " + vehiclePrice + " " + priceUnit +
                "\nCurrently being rented: " + rental +
                "\nAcquired: " + dateStr;
    }

    /**
     * Retrieves Vehicle data for a given Dealership.
     * <p>
     * This method fills a Map where each key-value pairs
     * represents the vehicle's attributes.
     *
     * @param map The Map to be filled with data from the Vehicle
     */
    public void getDataMap(Map<Key, Object> map) {
        Key.VEHICLE_ID.putNonNull(map, vehicleId);
        Key.VEHICLE_MANUFACTURER.putNonNull(map, vehicleManufacturer);
        Key.VEHICLE_MODEL.putNonNull(map, vehicleModel);
        Key.VEHICLE_PRICE.putNonNull(map, vehiclePrice);
        Key.VEHICLE_ACQUISITION_DATE.putNonNull(map, acquisitionDate);
        Key.VEHICLE_TYPE.putNonNull(map, vehicleType);
        Key.VEHICLE_RENTAL_STATUS.putNonNull(map, rental);
    }
}