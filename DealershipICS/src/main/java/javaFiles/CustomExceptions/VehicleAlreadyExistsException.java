package javaFiles.CustomExceptions;

public class VehicleAlreadyExistsException extends Exception {
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
