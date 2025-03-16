package javafiles.rentalstrategies;

import javafiles.customexceptions.RentalException;
import javafiles.domainfiles.Vehicle;

public interface RentalStrategy {

    void enableRental(Vehicle vehicle) throws RentalException;
    void disableRental(Vehicle vehicle) throws RentalException;

}
