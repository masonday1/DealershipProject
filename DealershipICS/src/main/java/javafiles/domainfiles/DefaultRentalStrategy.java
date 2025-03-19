package javafiles.domainfiles;

import javafiles.customexceptions.RentalException;
import javafiles.domainfiles.Vehicle;

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