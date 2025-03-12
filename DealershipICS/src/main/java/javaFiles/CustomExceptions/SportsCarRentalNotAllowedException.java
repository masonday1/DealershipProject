package javaFiles.CustomExceptions;

public class SportsCarRentalNotAllowedException extends RentalException {
    public SportsCarRentalNotAllowedException(String message) {
        super(message);
    }
}