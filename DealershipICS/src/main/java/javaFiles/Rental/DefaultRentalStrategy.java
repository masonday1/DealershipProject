package javaFiles.Rental;

import javaFiles.CustomExceptions.RentalException;
import javaFiles.Vehicle;

public class DefaultRentalStrategy implements RentalStrategy {
    @Override
    public void enableRental(Vehicle vehicle) throws RentalException
    {
        vehicle.setRental(true);
    }
    @Override
    public void disableRental(Vehicle vehicle) throws RentalException
    {
        vehicle.setRental(false);
    }


}