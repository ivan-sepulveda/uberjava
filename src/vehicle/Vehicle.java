package vehicle;

import location.Location;

import java.util.Scanner;

abstract class Vehicle {
    private Location currentLocation;

    /**
     *
     * @param args unsued
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Vehicle class? (y/n)");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")) {
            System.out.println("Testing code now:");
            System.out.println("Printing toString() of arbitrary car (Vehicle is abstract)");
            System.out.println((new Car("ArbNameModel", 2000, "ArbMake")).toString());
        }

    }

    /**
     *
     * @param startLocation is the currentLocation vehicle starts at
     */
    Vehicle(Location startLocation) {
        currentLocation = startLocation;
    }

    /**
     *
     * @param newLocation is the new currentLocation we want to drive the vehicle to
     * @return true if the Vehicle is moved false otherwise
     */
    public boolean driveTo(Location newLocation){
        currentLocation = newLocation;
        return (currentLocation.getName().equals(newLocation.getName()));
    }

    /**
     *
     * @return current vehicle currentLocation as a string
     */
    public Location getLocation(){
        return currentLocation;
    }

    /**
     *
     * @param currentLocation sets current location of vehicle
     */
    void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     *
     * @return the objects toString. "Vehicle: " + "Location at: " + currentLocation;
     */
    @Override
    public String toString(){
        return "Vehicle: " + "Location at: " + this.currentLocation;
    }
}
