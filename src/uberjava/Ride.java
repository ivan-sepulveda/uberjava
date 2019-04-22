package uberjava;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import location.Location;
import vehicle.Car;


class Ride {
    private double rideCost;
    private double amenities;
    private int rating;
    private String fareRequestURL;
    private double fare;
    private String rideNumber;
    private Location fromLocation;
    private Location toLocation;
    private String riderName;
    private String distanceTimeTollInfoURL;
    private String ratingURL;
    private double operatingCostOfRide;
    private String driverName;
    private int tolls;
    private int rideDurationMinutes;
    private int rideDistance;
    private WebReader reader;
    private int minutesToPickup;
    private int milesToPickup;
    private Driver driver;
    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Ride class? (y/n)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")){
            System.out.println("Testing code now:");
            System.out.println("Printing toString() of arbitrary Ride");
            System.out.println((new Ride(new Driver("John", true))).toString());
        }
    }

    /**
     *
     * @param driver is the driver for this ride
     */
    Ride(Driver driver) {
        this.driver = driver;
        this.driverName = driver.getName();
    }

    /**
     * sets values of essenial instance varieables
     */
    void setAllValues(){
        reader = new WebReader(driver);
        reader.setFareRequestURL();
        reader.parseMainRidePage();
        reader.parseDistanceTimeTollURL(reader.getDistanceTimeTollInfoURL());


        this.fareRequestURL = reader.getFareRequestURL();
        fare = reader.getFare();
        rideNumber = reader.getRideNumber();
        fromLocation = reader.getFromLocation();
        toLocation = reader.getToLocation();
        riderName = reader.getRiderName();
        tolls = reader.getTolls();
        amenities = reader.getAmenities();
        rideDurationMinutes = reader.getRideDurationMinutes();
        rideDistance = reader.getRideDistance();
    }

    /**
     * sets driver rating
     */
    void setRating(){
        reader.parseRatingURL(reader.getRatingURL());
        rating = reader.getRating();

    }

    /**
     *
     * @return driver rating for this rdie
     */
    int getRating() {
        return rating;
    }

    /**
     *
     * @return FareRequestURL as a string
     */
    public String getFareRequestURL() {
        return fareRequestURL;
    }

    /**
     *
     * @return ride fare
     */
    double getFare() {
        return fare;
    }

    /**
     *
     * @return ride number
     */
    String getRideNumber() {
        return rideNumber;
    }

    /**
     *
     * @return location of pickup
     */
    Location getFromLocation() {
        return fromLocation;
    }

    /**
     *
     * @return drop off location
     */
    Location getToLocation() {
        return toLocation;
    }

    /**
     *
     * @return distanceTimeTollInfoURL
     */
    public String getDistanceTimeTollInfoURL() {
        return distanceTimeTollInfoURL;
    }

    /**
     *
     * @return RatingURL
     */
    public String getRatingURL() {
        return ratingURL;
    }

    /**
     *
     * @return drivers name
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     *
     * @return tolls for ride (always whole dollar amount)
     */
    public int getTolls() {
        return tolls;
    }

    /**
     *
     * @return ride duration in minutes
     */
    public int getRideDurationMinutes() {
        return rideDurationMinutes;
    }

    /**
     *
     * @return ride distance in miles
     */
    public int getRideDistance() {
        return rideDistance;
    }

    /**
     *
     * @return WebReader object for this ride
     */
    public WebReader getWebReader() {
        return reader;
    }

    /**
     *
     * @return true if pickup location further than 300 miles, false otherwise
     */
    boolean isPickupRequestTooFar(){
        WebReader r = this.getWebReader();
        String baseURL = r.getDistanceTimeTollInfoURL();
        String[] toStringArray = driver.getDriversCar().getLocation().toString().split("\\[");
        String startCity = toStringArray[0].replace(" ", "+");
        String url = baseURL + "&start=" + startCity;
        String note = r.requestURLPageContent(url).split("Note: From ")[1];
        String[] splitNote = note.split("be ")[1].split("<")[0].split(" ");
        this.milesToPickup = Integer.parseInt(splitNote[0]);
        this.minutesToPickup = Integer.parseInt(splitNote[4]);
        return (minutesToPickup > 300);
    }

    /**
     *
     * @return how long it takes from cars parked location to pickup location
     */
    public int getMinutesToPickup() {
        return minutesToPickup;
    }

    /**
     *
     * @return distance from cars parked location to pickup location
     */
    int getMilesToPickup() {
        return milesToPickup;
    }

    /**
     * sets ride cost and associated cost likes tolls
     */
    public void setRideCost() {
        rideCost += tolls;


        double opCostPickup = (driver.getDriversCar().getOperatingPricePerMinute()*minutesToPickup);
        double opCostTrip = (driver.getDriversCar().getOperatingPricePerMinute()*rideDurationMinutes);
        setOperatingCostOfRide((opCostPickup+opCostTrip));
    }



    /**
     *
     * @return the ride cost
     */
    double getRideCost() {
        return rideCost;
    }

    /**
     *
     * @return price of amenities
     */
    double getAmenities() {
        return amenities;
    }

    /**
     *
     * @param operatingCostOfRide is the operating cost of ride per time period
     */
    void setOperatingCostOfRide(double operatingCostOfRide) {
        this.operatingCostOfRide = operatingCostOfRide;
    }

    /**
     *
     * @return operating cost of ride as a double (car operating cost i.e car maintenance)
     */
    double getOperatingCostOfRide() {
        return operatingCostOfRide;
    }

    /**
     *
     * @return driver of this ride (as a Driver object)
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     *
     * @return ride info as a string
     */
    @Override
    public String toString(){
        return "Ride: " + fromLocation + " to " + toLocation + ". Driver: " + getDriverName() + "Rider: " + riderName;
    }
}
