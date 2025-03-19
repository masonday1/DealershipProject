package javafiles.domainfiles;

/**
 * Pickup is a child class of Vehicle. Its constructor is called by {@link VehicleCreator#createVehicle(String, String)}.
 *
 * @author Christopher Engelhart
 */

public class Pickup extends Vehicle
{
    /**
     * Constructs a new Pickup object.
     * Invokes the superclass constructor with "Pickup" as the vehicle type,
     * and sets the vehicle ID using {@link Vehicle#setVehicleId(String)}.
     *
     * @param vehicleID The vehicle ID of the Pickup to be created.
     */
    public Pickup(String vehicleID)
    {
        super("Pickup");
        this.setVehicleId(vehicleID);
    }

}