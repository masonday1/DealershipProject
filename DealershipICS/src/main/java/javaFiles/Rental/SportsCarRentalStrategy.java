package javaFiles.Rental;

import javaFiles.CustomExceptions.SportsCarRentalNotAllowedException;
import javaFiles.Vehicle;

public class SportsCarRentalStrategy implements RentalStrategy {
    @Override
    public void enableRental(Vehicle vehicle) throws SportsCarRentalNotAllowedException {
        throw new SportsCarRentalNotAllowedException("Sports cars cannot be rented.");
    }

    @Override
    public void disableRental(Vehicle vehicle) throws SportsCarRentalNotAllowedException
    {
        throw  new SportsCarRentalNotAllowedException("Sports car rental features cannot be changed");
    }
}
