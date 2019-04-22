package vehicle;

import location.Location;
import exception.ValueUndefinedException;

import java.util.ArrayList;

public class Car extends Vehicle{
    private static ArrayList<Car> allCars = new ArrayList<>();
    private boolean isParked;
    private String name;
    private int year;
    private String make;
    private Double milesPerGallon;
    private Integer purchaseCost = null;
    private double operatingPricePerMinute;

    /**
     *
     * @param args main method to test Car class
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Car class? (y/n)");
    }

    /**
     *
     * for this constructor, default location will be san francisco
     * @param name car name/ aka model
     * @param year car manufacture year
     * @param make car make (company that made car
     *
     */
    Car(String name, int year, String make){
        super(Location.getByName("San Francisco"));
        this.name = name;
        this.year = year;
        this.make = make;
        this.isParked = true;
        allCars.add(this);
    }
    /**
     *
     * for this constructor, you can input a different location
     * @param name car name/ aka model
     * @param year car manufacture year
     * @param make car make (company that made car
     *
     */
    Car(String name, int year, String make, Location location) {
        super(location);
        this.name = name;
        this.year = year;
        this.make = make;
        allCars.add(this);

    }

    /**
     *
     * @param milesPerGallon is the miles that the car gets per mile
     */
    void setMilesPerGallon(double milesPerGallon) {
        this.milesPerGallon = milesPerGallon;
    }

    /**
     *
     * @param purchaseCost is the purchase cost of the car MSRP
     */
    void setpurchaseCost(Integer purchaseCost) {
        if (purchaseCost == null){
            throw new ValueUndefinedException("Purchase cost has not been set yet!");
        }
        this.purchaseCost = purchaseCost;
    }

    /**
     * set the operating cost per minute. No params needed
     */
    public void setOperatingPricePerMinute() {
        double operatingPricePerYear = purchaseCost * 0.20;
        double operatingPricePerDay = operatingPricePerYear/365;
        double operatingPricePerHour = operatingPricePerDay/24;
        this.operatingPricePerMinute = operatingPricePerHour/60;
    }

    /**
     *
     * @return miles per gallon of the car as a double
     * @throws ValueUndefinedException if it hasn't been set yet
     */
    public double getMilesPerGallon() {
        if (milesPerGallon == null){
            throw new ValueUndefinedException("milesPerGallon not set yet!");
        }
        return milesPerGallon;
    }


    /**
     *
     * @return purchase cost of the car
     */
    public int getPurchaseCost() {
        return purchaseCost;
    }

    /**
     *
     * @param purchaseCost is the purchase cost or MSRP of the car
     */
    public void setPurchaseCost(int purchaseCost){
        this.purchaseCost = purchaseCost;
    }

    /**
     *
     * @return
     */
    public double getOperatingPricePerMinute() {
        return operatingPricePerMinute;
    }

    /**
     *
     * @return Array of all cares created
     */
    public static Car[] getCars(){
        Car[] allCreatedCars = new Car[allCars.size()];
        for (int i=0; i<allCars.size(); i++){
            allCreatedCars[i] = allCars.get(i);
        }
        return allCreatedCars;
    }

    /**
     *
     * @param newLocation is the new currentLocation we want to drive the vehicle to
     * @return true if car moved false otherwise
     */
    @Override
    public boolean driveTo(Location newLocation) {

        if (isParked) {
            this.isParked = false;
            this.setCurrentLocation(newLocation);
            return true;
        } else {
            return false;
        }
//        return (!this.currentLocation.toString().equals(oldLocationToString));
    }

    /**
     *
     * @return name of car
     */
    public String getName() {
        return name;
    }

    /**
     * sets isParked variable to true
     */
    public void park(){
        isParked = true;
    }

    /**
     *
     * @return true if parked false otherwise
     */
    boolean parked(){
        return isParked;
    }

    /**
     *
     * @return true if parked false otherwise
     */
    boolean isParked(){
        return isParked;
    }

    @Override
    public String toString(){
        return name + " " + make + " " + name + ": " + milesPerGallon + "mpg / $"  + purchaseCost + " MSRP";
    }
}
