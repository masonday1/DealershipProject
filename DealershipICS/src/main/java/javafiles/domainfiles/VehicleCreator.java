package javafiles.domainfiles;
import javafiles.customexceptions.InvalidVehicleTypeException;

/**
 * A factory class for creating {@link Vehicle} objects.
 * Implements the Singleton pattern to ensure only one instance exists.
 */
class VehicleCreator implements VehicleFactory {

    private static final VehicleCreator instance = new VehicleCreator();

    private VehicleCreator() {} // Private constructor

    public static VehicleCreator getInstance() {
        return instance;
    }

    /**
     * Creates a {@link Vehicle} object based on the given vehicle type.
     *
     * @param vehicleType The type of vehicle to create ("suv", "sedan", "pickup", "sports car").
     * @param vehicleID The ID of the vehicle to create.
     * @return A {@link Vehicle} object of the specified type.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     */
    @Override
    public Vehicle createVehicle(String vehicleType, String vehicleID) throws InvalidVehicleTypeException {
        return switch (vehicleType.toLowerCase()) {
            case "suv" -> new SUV(vehicleID);
            case "sedan" -> new Sedan(vehicleID);
            case "pickup" -> new Pickup(vehicleID);
            case "sports car" -> new SportsCar(vehicleID);
            default ->  throw new InvalidVehicleTypeException("Unsupported vehicle type: " + vehicleType
            + " " + vehicleID + " unable to be created");
        };
    }
}