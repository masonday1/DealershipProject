package javaFiles;

import javaFiles.Rental.SportsCarRentalStrategy;

/** SportsCar is a child class of Vehicle, it calls Vehicle class's constructor
 * with the string "Sports car" , which then sets a SportsCar object's vehicleType
 * to "SportsCar"
 *
 * @author Christopher Engelhart
*/

public class SportsCar extends Vehicle
{
    /**
     * Sports_car constructor calls upon its parent class Vehicle's constructor.
     * Sports_car constructor passes the String "Sports_car" to Vehicle's constructor.
     */
    SportsCar()
    {
        super("Sports car", new SportsCarRentalStrategy());
    }
}
