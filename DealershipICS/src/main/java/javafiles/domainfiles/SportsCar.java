package javafiles.domainfiles;

import javafiles.rentalstrategies.SportsCarRentalStrategy;

/**
 * SportsCar is a child class of Vehicle. It represents a sports car vehicle,
 * which has a special rental strategy that prevents it from being rented.
 * The constructor initializes the vehicle with the "Sports car" type and the
 * {@link SportsCarRentalStrategy}. SportsCar constructor is called by {@link VehicleCreator#createVehicle(String, String).
 *
 * @author Christopher Engelhart
 */

public class SportsCar extends Vehicle
{
    /**
     * Constructs a new SportsCar object.
     * Invokes the superclass constructor with "Sports car" as the vehicle type
     * and a {@link SportsCarRentalStrategy} to prevent rentals.
     * Sets the vehicle ID using {@link Vehicle#setVehicleId(String)}.
     *
     * @param vehicleID The vehicle ID of the SportsCar to be created.
     */
    SportsCar(String vehicleID)
    {
        super("Sports car", new SportsCarRentalStrategy());
        this.setVehicleId(vehicleID);
    }
}
