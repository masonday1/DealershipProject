package javafiles.rentalstrategies;

import javafiles.customexceptions.SportsCarRentalNotAllowedException;
import javafiles.domainfiles.Vehicle;

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
