package uberjava;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Rider {
    private String name;
    private static final String tab = "    ";
    private double totalFaresTotal = 0.0;
    private int totalMinutesSpentInUber = 0;
    private static ArrayList<Rider> allRiders = new ArrayList<>();
    private HashMap<String, Integer> ridesPerDriver = new HashMap<>();

    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args) {
        System.out.println("Do you want to test the Rider class? (y/n)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")){
            System.out.println("Testing code now:");
            System.out.println("Printing toString() of arbitrary Rider Harry Potter");
            System.out.println(new Rider("Harry Potter"));
        }
    }

    /**
     *
     * @param name is the name of the rider aka passenger
     */
    Rider(String name) {
        this.name = name;
        allRiders.add(this);
    }

    /**
     *
     * @param ride is the ride we are assigning to the riders profile
     */
    void addRideToRider(Ride ride) {
        totalFaresTotal += 1;
        totalFaresTotal += ride.getFare();
        totalMinutesSpentInUber += ride.getRideDurationMinutes();
        totalFaresTotal += ride.getFare();

        String driverName = ride.getDriver().getName();
        boolean newDriver = (!ridesPerDriver.keySet().contains(driverName));
        if (newDriver) {
            ridesPerDriver.put(ride.getDriver().getName(), 1);
        } else {
            int prevRidesWithThisDriver = ridesPerDriver.get(driverName);
            ridesPerDriver.put(driverName, prevRidesWithThisDriver + 1);
        }
    }

    /**
     *
     * @return riders name
     */
    private String getName() {
        return name;
    }

    /**
     *
     * @return array list of all riders
     */
    public static ArrayList<Rider> getAllRiders() {
        return allRiders;
    }

    /**
     *
     * @param riderName is the name of the rider we want to get
     * @return null of no rider by that name, otherwise returns corresponding rider object
     */
    static public Rider getRiderByName(String riderName) {
        for (Rider r : allRiders) {
            if (r.getName().equals(riderName)) {
                return r;
            }
        }
        return null;
    }

    /**
     *
     * @param rider the rider whos stats we want to print
     */
    static void printRiderStats(Rider rider) {
        System.out.println(rider.getName() + ": " + rider.totalFares());
        for (String driverName : rider.getRidesPerDriver().keySet()) {
            System.out.println(tab+ driverName + ": " + rider.getRidesPerDriver().get(driverName));
        }
        System.out.println(tab+tab+"Total paid to UberJava: $" + String.format("%.2f", rider.getTotalFaresTotal()));
        System.out.println(tab+tab+"Total minutes in UberJava rides: " + rider.getTotalMinutesSpentInUber());
    }

    /**
     * prints stats of all riders in our static array list
     */
    public static void printAllRiderStats() {
        for (Rider r : allRiders) {
            printRiderStats(r);
//            System.out.println("\n");
        }
    }

    /**
     *
     * @return hashmap of all drivers riders rode with, and how many times they got the same driver
     */
    public HashMap<String, Integer> getRidesPerDriver() {
        return ridesPerDriver;
    }

    /**
     *
     * @param riderName is the rider name
     * @return true is new rider, false if we've encountered this rider before
     */
    static boolean isThisNewRider(String riderName) {
        for (Rider r : allRiders) {
            if (r.getName().equals(riderName)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return total fares this rider has paid
     */
    public double getTotalFaresTotal() {
        return totalFaresTotal;
    }

    /**
     *
     * @return total minutes this rider has spent in rides
     */
    public int getTotalMinutesSpentInUber() {
        return totalMinutesSpentInUber;
    }

    /**
     *
     * @return total fares this rider has paid
     */
    int totalFares(){
        int totalRides = 0;
        for (String driverName : this.getRidesPerDriver().keySet()) {
            totalRides +=this.getRidesPerDriver().get(driverName);
        }
        return totalRides;
    }

    /**
     *
     * @return rider information as a toString
     */
    @Override
    public String toString(){
        return "Rider: " + name + ". Total Rides so far: " + totalFaresTotal;
    }
}