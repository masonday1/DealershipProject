package javaFiles.Rental;

import javaFiles.CustomExceptions.RentalException;
import javaFiles.Vehicle;

public interface RentalStrategy {

    void enableRental(Vehicle vehicle) throws RentalException;
    void disableRental(Vehicle vehicle) throws RentalException;

}
