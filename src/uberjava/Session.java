package uberjava;
import java.net.*;
import java.io.*;

import location.Location;
import vehicle.Car;
import vehicle.Tesla_Roadster_2019;

public class Session{

    private Ride lastRide;

    private double effectiveHourlyEarnings;

    private double totalAmenities = 0.0;
    private double totalGasCost = 0.0;

    private double totalAutoLoanPaymentsPerHour = 0;
    private int totalTolls;
    private int ridesSoFar = 0;
    private int minutesSpentGivingRides = 0;
    private int totalSessionMinutes = 0;

    private int totalMilesDrivenThisSesion = 0;
    private int totalMilesDrivingPassengers = 0;

    private static final double priceOfGallonOfGas = 3.39;
    private double sessionHours = 0;
    private double amountMadeThisSession = 0.0;
    private double totalFares;
    private double totalSessionCostToDriver = 0.0;
    private double totalOperatingCostForThisSession;


    private Driver driver;
    private Car car;
    private UberStatistics stats = new UberStatistics();
    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Session class? (y/n)");
        System.out.println("Creating new arb session and printing toString()");
        System.out.println((new Session(new Driver("Ivan", false), new Tesla_Roadster_2019())));
    }

    /**
     * constructor
     * @param driver driver of car at the time
     * @param driversCar the drivers car
     */
    Session(Driver driver, Car driversCar){
        this.driver = driver;
        car = driversCar;
    }

    /**
     * acknoledges ride request, then decides wheter to accept or reject
     */
    void regardRideRequest(){
        Ride currentRide = new Ride(driver);
        currentRide.setAllValues();
        boolean startLocSameAsParkedLoc = car.getLocation().toString().equals(currentRide.getFromLocation().toString());

        if (startLocSameAsParkedLoc){
            rejectRide(currentRide);
        }
        if ((!startLocSameAsParkedLoc) && currentRide.isPickupRequestTooFar()){
            rejectRide(currentRide);
        }
        else{
            currentRide.setRating();
            addRide(currentRide);
            car.driveTo(currentRide.getToLocation());
            lastRide = currentRide;
            car.park();
        }
    }

    /**
     *
     * @param acceptedRide is the ride we are accepting
     */
    void addRide(Ride acceptedRide){
        double gasCostPerMile = Session.getPriceOfGallonOfGas()/driver.getDriversCar().getMilesPerGallon();

        // All Driver Info

        // add to total number of rides
        ridesSoFar++;
        // add the rides rating to the statistics instance
        stats.addRating(acceptedRide.getRating());
        // add to the time spent actually giving passengers a ride
        minutesSpentGivingRides += acceptedRide.getRideDurationMinutes();
        // add to the total session minutes (which includes pickup)
        totalSessionMinutes += acceptedRide.getRideDurationMinutes() + acceptedRide.getMinutesToPickup();
        // add to the total fares (what the passenger paid)
        totalFares += acceptedRide.getFare();
        // add toll(s) (if any) total tolls
        totalTolls += acceptedRide.getTolls();
        // add to the amount I actually made (total fair times 0.75, because that's my cut)
        amountMadeThisSession += (acceptedRide.getFare() * 0.75);
        // set what the ride cost the driver
        acceptedRide.setRideCost();
        // add total amenities if driver is premium
        if (driver.isPremiumDriver()){
            totalAmenities += acceptedRide.getAmenities();
        }
        totalGasCost += gasCostPerMile*acceptedRide.getMilesToPickup();
        totalGasCost += gasCostPerMile*acceptedRide.getRideDistance();
        totalOperatingCostForThisSession += acceptedRide.getOperatingCostOfRide();
        totalMilesDrivenThisSesion += acceptedRide.getMilesToPickup();
        totalMilesDrivenThisSesion += acceptedRide.getRideDistance();
        totalMilesDrivingPassengers += acceptedRide.getRideDistance();


        // All rider
        String riderName = acceptedRide.getWebReader().getRiderName();
        if (Rider.isThisNewRider(riderName)) {
            Rider newRider = new Rider(riderName);
            newRider.addRideToRider(acceptedRide);
        }
        else {
            Rider.getRiderByName(riderName).addRideToRider(acceptedRide);
        }
    }

    /**
     *
     * @return nubmer of rides for this session so far
     */
    public int getRidesSoFar() {
        return ridesSoFar;
    }

    int endSession(){
        setSessionHours();
        setTotalSessionCostToDriver();
        setTotalAutoLoanPaymentsPerHour(getSessionHours());
        setEffectiveHourlyEarnings();
        return 1;
    }

    void setSessionHours(){
        sessionHours = totalSessionMinutes/60.0;
    }





    void rejectRide(Ride ride){
        WebReader r = ride.getWebReader();
        r.requestURLPageContent("https://www.cs.usfca.edu/~dhalperin/reject.cgi?rideNumber=" + ride.getRideNumber());
    }

    int getSessionMinutes() {
        return totalSessionMinutes;
    }

    Ride getLastRide() {
        return lastRide;
    }

    public double getAmountMadeThisSession() {
        return amountMadeThisSession;
    }

    public Driver getDriver() {
        return driver;
    }

    static double getPriceOfGallonOfGas() {
        return priceOfGallonOfGas;
    }

    public double getTotalFares() {
        return totalFares;
    }

    public static void printSessionSummary(Session session){
        /*
        Driver name and whether they are UberJavaX Premium
        Number of fares they had
        Total fares, total miles driven, and total hours/minutes word
        Total amount the driver earned.
        Total cost of operation (with a breakdown by type of cost)
        Average Rider Rating
        Effective Hourly Earnings after all costs are deducted.
        Also, at the end, print the record for each Rider as follows:
        Total number of fares they had in total and per Driver
        Total amount they paid UberJava
        Total minutes that they spent on UberJava rides
         */

        // Driver name and whether they are UberJavaX Premium
        String tab = "    ";
        String driverType = "";
        String costVO = "Vehicle ownership: $";
        String opCost = "Total Operating Cost: $";


        driverType += session.getDriver().isPremiumDriver() ? "UberJavaX Premium" : "Regular (NOT UberJavaX Premium)";
        System.out.println("Driver: " + session.getDriver().getName() + " - " + driverType);
        System.out.println("Total Number of Fares: " + session.getRidesSoFar());
        System.out.println("Total Fares: " + round2(session.getTotalFares()));
        System.out.println("Total Miles Driven (Including Pickups): "+ session.getTotalMilesDrivenThisSesion());
        System.out.println("Total Miles Driven (Excluding Pickups): "+ session.getTotalMilesDrivingPassengers());
        System.out.println("Minutes Worked: " + session.getSessionMinutes());
        System.out.println("Hours Worked: " + round2(session.getSessionMinutes()/60.0));
        System.out.println("Minutes Spent Driving Passengers: "+session.getMinutesSpentGivingRides());
        System.out.println("Hours Spent Driving Passengers: " + round2(session.getMinutesSpentGivingRides()/60.0));
        System.out.println("Total amount earned: $" +String.format("%.2f",session.getAmountMadeThisSession()));
        System.out.println("Total cost of operation : $" + String.format("%.2f",session.getTotalSessionCostToDriver()));
        System.out.println(tab+opCost+String.format("%.2f",session.getTotalOperatingCostForThisSession()));
        System.out.println(tab+costVO+String.format("%.2f",session.getTotalAutoLoanPaymentsPerHour()));
        System.out.println(tab+"Total Tolls: $" + String.format("%.2f", (double)session.getTotalTolls()));
        System.out.println(tab+"Total Gas: $" + round2(session.getTotalGasCost()));
        if (session.getDriver().isPremiumDriver()){
            System.out.println(tab+"Total Amenities: $" + String.format("%.2f", session.getTotalAmenities()));
        }
        Session.printNotes();
        System.out.println("Average Rider Rating: " + String.format("%.2f", session.getStats().getRatingAverage()));
        System.out.println("Effective Hourly Earnings: $" + String.format("%.2f",session.getEffectiveHourlyEarnings()));

    }

    public int getMinutesSpentGivingRides() {
        return minutesSpentGivingRides;
    }

    private int getTotalMilesDrivenThisSesion() {
        return totalMilesDrivenThisSesion;
    }

    private int getTotalMilesDrivingPassengers() {
        return totalMilesDrivingPassengers;
    }

    public int getTotalSessionMinutes() {
        return totalSessionMinutes;
    }

    public double getSessionHours() {
        return sessionHours;
    }



    public double getTotalSessionCostToDriver() {
        return totalSessionCostToDriver;
    }

    public Car getCar() {
        return car;
    }

    public UberStatistics getStats() {
        return stats;
    }

    public int getTotalTolls() {
        return totalTolls;
    }

    public double getTotalAmenities() {
        return totalAmenities;
    }

    public double getTotalGasCost() {
        return totalGasCost;
    }

    public static double round2(double d){
        return (Math.round(d * 100.0) / 100.0);
    }

    public void setTotalSessionCostToDriver() {
        if (driver.isPremiumDriver()){
            totalSessionCostToDriver += getTotalAmenities();
        }
        totalSessionCostToDriver += getTotalGasCost();
        totalSessionCostToDriver += getTotalTolls();
        totalSessionCostToDriver += totalOperatingCostForThisSession;
        totalSessionCostToDriver += totalAutoLoanPaymentsPerHour;
    }

    private void setTotalAutoLoanPaymentsPerHour(double unRoundedHours) {
        this.totalAutoLoanPaymentsPerHour = (driver.getLoanCostPerHour()*(int)Math.ceil(unRoundedHours));
    }

    private double getTotalAutoLoanPaymentsPerHour() {
        return totalAutoLoanPaymentsPerHour;
    }

    private double getTotalOperatingCostForThisSession() {
        return totalOperatingCostForThisSession;
    }

    static void printNotes(){
        String tab = "    ";
        String note1 = "Note #1: All gas cost are based off a price of $" + Session.getPriceOfGallonOfGas();
        note1 += " per gallon.";
        String note2 = "Note #2: 'Total Cost of Operation' != 'Vehicle operating cost'. (Although V.O.C is included ";
        note2 += "in overarching T.O.C)";
        String note3 = "Note #3: Auto Loan Payments are based on session hours (always rounded up, i.e ";
        note3 += "if session was 72.31 hours we'd base Auto Loan Payments off 73 hours.";
        System.out.println(tab+note1);
        System.out.println(tab+note2);
        System.out.println(tab+note3);

    }

    private void setEffectiveHourlyEarnings() {
        this.effectiveHourlyEarnings = amountMadeThisSession/getSessionHours();
    }


    private double getEffectiveHourlyEarnings() {
        return effectiveHourlyEarnings;
    }

    @Override
    public String toString(){
        return "Session Info:\n"+"Driver: "+driver.getName()+"\nTot-Miles: "+getTotalMilesDrivenThisSesion();
    }
}