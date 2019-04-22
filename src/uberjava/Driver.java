package uberjava;

import location.Location;
import vehicle.Car;

import java.util.ArrayList;
import java.util.Scanner;

public class Driver{
    private static int totalSessions = -1;
    private Session currentSession = null;
    private Session lastSession = null;
    private String name;
//    private boolean currentlyInASession = false;
    private Car car;
    private double autoLoanPeriod = 3.0;
    private double yearlyInterestPercentage = 7.0;
    private static ArrayList<UberStatistics> allStats = new ArrayList<>();
    private boolean isPremiumDriver;
    private double loanCostPerHour;

    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Driver class? (y/n)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")){
            System.out.println("Testing code now:");
            System.out.println("Printing toString() of arbitrary Premium driver John");
            System.out.println((new Driver("John", true)).toString());
        }
    }

    /**
     *
     * @param name name of driver
     * @param isPremiumDriver true if driver is premium, false otherwise
     */
    public Driver(String name, boolean isPremiumDriver) {
        this.name = name;
        this.isPremiumDriver = isPremiumDriver;
        this.setAutoLoanPeriod(3.0);
        this.setYearlyInterestPercentage(7.0);
    }

    /**
     *
     * @param car is the car asssigned to the driver
     */
    public void assignCar(Car car){
        this.car = car;
        double principal = car.getPurchaseCost();
        double interestRate = (this.yearlyInterestPercentage/(100.00))/(12);
        double totalMonthlyInstallments = autoLoanPeriod*12.0;
        double numerator = principal * interestRate * Math.pow(1+interestRate, totalMonthlyInstallments);
        double denom = Math.pow(1+interestRate, totalMonthlyInstallments) - 1;
        double emi = Math.round(numerator/denom * 100.0) / 100.0;
        this.loanCostPerHour = emi/(30*24);
    }

    /**
     *
     * @return name of driver
     */
    String getName() {
        return name;
    }

//    public boolean isCurrentlyInASession() {
//        return currentlyInASession;
//    }

    /**
     *
     * @return true if driver premium, false otherwise
     */
    boolean isPremiumDriver() {
        return isPremiumDriver;
    }

    /**
     *
     * @return session number
     * @throws IllegalStateException if already a session in progress
     */
    public int startSession() throws IllegalStateException{
        if (currentSession != null){
            Exception e = new IllegalStateException();
            System.out.println("There's already a session in progress!");
            e.printStackTrace();
            throw (new IllegalStateException());
        }
        totalSessions += 1;
        setCurrentSession(new Session(this, car));
//        currentSession = ;
        boolean driven24Hours = false;
        boolean isLastRideToSF = false;
        boolean canIEndSession = (driven24Hours && isLastRideToSF && (currentSession.getLastRide()!= null));


        while (!canIEndSession){
            currentSession.regardRideRequest();
//            System.out.println("Session so far at: " + currentSession.getSessionMinutes() + "/1440 minutes");
            driven24Hours = (currentSession.getSessionMinutes() > 1440);
            if (currentSession.getLastRide() != null){
                if (currentSession.getLastRide().getToLocation() != null){
                    isLastRideToSF = currentSession.getLastRide().getToLocation().toString().contains("San Francisco");
                    canIEndSession = (driven24Hours && isLastRideToSF);

                }
            }
        }
//        currentlyInASession = false;
        lastSession = currentSession;
        endSession();

        return totalSessions;
    }

    /**
     *
     * @return session number
     * @throws IllegalStateException if no session available to end
     */
    private int endSession() throws IllegalStateException{
        if (currentSession == null){
            Exception e = new IllegalStateException();
            System.out.println("There's no session in progress to end!");
            e.printStackTrace();
            throw (new IllegalStateException());
        }
        allStats.add(currentSession.getStats());

        currentSession.endSession();
        return totalSessions;
    }

    /**
     *
     * @param autoLoanPeriod is auto loan period in years
     */
    private void setAutoLoanPeriod(double autoLoanPeriod) {
        this.autoLoanPeriod = autoLoanPeriod;
    }


    /**
     *
     * @param yearlyInterestPercentage auto loan interest per year
     */
    private void setYearlyInterestPercentage(double yearlyInterestPercentage) {
        this.yearlyInterestPercentage = yearlyInterestPercentage;
    }

    /**
     *
     * @return most previous session driver ahd
     */
    public Session getLastSession() {
        return lastSession;
    }

    /**
     *
     * @return loan cost per hour
     */
    double getLoanCostPerHour() {
        return loanCostPerHour;
    }

    /**
     *
     * @param currentSession is drivers current session
     */
    private void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    /**
     *
     * @return drivers Car object
     */
    Car getDriversCar(){
        return car;
    }

    /**
     *
     * @param sessionNumber session number we want
     * @return UberStatistics object for this driver input session
     */
    UberStatistics getSessionStatistics(int sessionNumber){
        System.out.println("Statistics for Session: " + sessionNumber);

        try {
            return allStats.get(sessionNumber);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Session " + sessionNumber + " does not exist!");
            return null;
        }
    }

    /**
     *
     * @return session stats for drives most recent session.
     */
    UberStatistics getSessionStatistics(){
        System.out.println("Statistics for Session: " + totalSessions);
        if (currentSession !=  null){
            return currentSession.getStats();
        }
        if (lastSession != null){
            return lastSession.getStats();
        }
        else  {
            return null;
        }
    }

    /**
     *
     * @return current driver location
     */
    Location getCurrentLocation(){
        return car.getLocation();
    }

    /**
     *
     * @return total session minutes
     * @throws IllegalStateException if no sesion to pull minutes from
     */
    int getSessionMinutes() throws IllegalStateException{
        if (currentSession == null){
            throw new IllegalStateException();
        }
        return currentSession.getSessionMinutes();
    }

    /**
     *
     * @return driver name and premium status
     */
    @Override
    public String toString() {
        if (isPremiumDriver) {
            return name + "UberJava Premium";

        } else {
            return name + " Regular Driver (NOT UberJava Premium)? : ";
        }
    }
}