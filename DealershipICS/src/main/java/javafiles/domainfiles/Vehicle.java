package javafiles.domainfiles;

import javafiles.customexceptions.*;
import javafiles.dataaccessfiles.JSONIO;
import javafiles.rentalstrategies.*;
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
    public void setVehiclePrice(long vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    /**
     * Sets the acquisition date of the vehicle.
     *
     * @param acquisitionDate the date the vehicle was acquired
     */
    public void setAcquisitionDate(long acquisitionDate) { this.acquisitionDate = acquisitionDate;
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
    public long getVehiclePrice() {return vehiclePrice;}
    public long getAcquisitionDate() {return acquisitionDate;}
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
        Date date = new Date(acquisitionDate);
        return  "Vehicle: " +  vehicleType +
                "\nID: " + vehicleId +
                "\nManufacturer " + vehicleManufacturer +
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
        map.put(JSONIO.getTypeKey(), vehicleType);
        map.put(JSONIO.getManufacturerKey(), vehicleManufacturer);
        map.put(JSONIO.getModelKey(), vehicleModel);
        map.put(JSONIO.getVehicleIdKey(), vehicleId);
        map.put(JSONIO.getPriceKey(), vehiclePrice);
        map.put(JSONIO.getDateKey(), acquisitionDate);
    }
}