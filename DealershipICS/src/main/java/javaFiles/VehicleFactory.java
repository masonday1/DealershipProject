package javaFiles;

import javaFiles.CustomExceptions.InvalidVehicleTypeException;

/**
 * Defines the contract for a factory that creates {@link Vehicle} objects.
 * Implementations of this interface are responsible for instantiating specific
 * types of vehicles based on a provided type string.
 */
interface VehicleFactory {
    Vehicle createVehicle(String vehicleType, String vehicleID) throws InvalidVehicleTypeException;
}