package uberjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import location.Location;


class WebReader {
    private int rating;
    private String fareRequestURL;
    private double fare;
    private String rideNumber;
    private Location fromLocation;
    private Location toLocation;
    private String riderName;
    private String distanceTimeTollInfoURL;
    private String ratingURL;
    private String driverName;
    private int tolls;
    private int rideDurationMinutes;
    private int rideDistance;
    private double amenities = 0.0;


    private Driver driver;

    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the WebReader class? (y/n)");
    }

    /**
     * constructor
     * @param driver who is driving the vehicle in this case
     */
    WebReader(Driver driver) {
        this.driver = driver;
        this.driverName = driver.getName();
    }

    /**
     * sets FareRequestURL
     */
    void setFareRequestURL() {
        String reformattedName = driverName.replace(" ", "+");
        fareRequestURL = "https://www.cs.usfca.edu/~dhalperin/nextFare.cgi?driver=" + reformattedName + "";
    }

    /**
     * parses main ride page, sets instance variables
     */
    void parseMainRidePage(){
        String letUsKnowString = "Let us know you've completed the trip by reporting to:\n";
        String thenWellLetYouKnowString = "Then we'll let you know how";
        String fareIsString = "Fare is ";
        String content = requestURLPageContent(fareRequestURL);
        int indexOfFirstParagraphStart = content.indexOf("<p>");
        int indexOfFirstParagraphEnd = content.indexOf("</p>") + 4; // plus 4 because of 3 additional characters /p>
        int indexOfFirstLinkStart = content.indexOf("https:");
        int indexOfFirstLinkEnd = content.indexOf("Let us know");
        String rawDistanceTimeTollInfo = content.substring(indexOfFirstLinkStart, indexOfFirstLinkEnd);
        String distanceTimeTollInfo = rawDistanceTimeTollInfo.split("<")[0];
        distanceTimeTollInfoURL = distanceTimeTollInfo;
        rideNumber = distanceTimeTollInfo.substring(distanceTimeTollInfo.indexOf("=")+1).trim();
        String rawRatingURL = content.substring(content.indexOf(letUsKnowString) + letUsKnowString.length());
        rawRatingURL = rawRatingURL.substring(0, rawRatingURL.indexOf(thenWellLetYouKnowString));
        rawRatingURL = rawRatingURL.substring(rawRatingURL.indexOf(">")+1);
        String ratingURL = rawRatingURL.split("<")[0];
        this.ratingURL = ratingURL;
        String rideInfo = (content.substring(indexOfFirstParagraphStart, indexOfFirstParagraphEnd+4));
        rideInfo = rideInfo.substring(rideInfo.indexOf(">")+1);
        rideInfo = rideInfo.substring(0, rideInfo.indexOf("</p>"));
        String[] rideInfoArrray = rideInfo.split("\n");
        for (String e: rideInfoArrray){
            if (e.contains("Fare")){
                String removeBreak = e.trim().split("<br/>")[0];
                String breakRemoved = java.util.regex.Pattern.quote(removeBreak).replace("\\E", "");
                breakRemoved = breakRemoved.substring(breakRemoved.indexOf(fareIsString) + fareIsString.length()+1);
                fare = Double.parseDouble(breakRemoved);
            }
            if (e.contains("Going FROM")){
                String removeBreak = e.trim().split("<br/>")[0];
                String breakRemoved = java.util.regex.Pattern.quote(removeBreak).replace("\\E", "");
                breakRemoved = breakRemoved.substring(breakRemoved.indexOf(fareIsString) + fareIsString.length()+1);
                breakRemoved = breakRemoved.substring(5);
                String[] toFromString = breakRemoved.split(" to ");
                String fromLocationString = toFromString[0].trim();
                fromLocation = Location.getByName(fromLocationString);
                String toLocationString = toFromString[1].trim();
                toLocation = Location.getByName(toLocationString);
            }
            if (e.contains("Rider:")){
                String removeBreak = e.trim().split("<br/>")[0];
                String breakRemoved = java.util.regex.Pattern.quote(removeBreak).replace("\\E", "");
                breakRemoved = breakRemoved.substring(breakRemoved.indexOf(":")+1, breakRemoved.lastIndexOf("<"));
                riderName = breakRemoved.trim();
            }
        }
    }

    /**
     *
     * @param inputURL is the DistanceTimeTollURL
     * @return String containing all relevant info
     */
    String parseDistanceTimeTollURL(String inputURL){
        String[] toStringArray = driver.getDriversCar().getLocation().toString().split("\\[");
        String startCity = toStringArray[0].replace(" ", "+");
        String url = inputURL + "&start=" + startCity;

        String distanceIndicater = "Distance: ";
        String tollIndicator = "Tolls: $";
        String minuteIndicator = "Minutes: ";

        String content = requestURLPageContent(url);

        if (content.contains("Start location cannot be specified")){
            content = requestURLPageContent(inputURL);
        }
        content = content.substring(content.lastIndexOf("Distance: "));


        String[] contentAsArray = content.split("\n");
        for (String s: contentAsArray){
            String cleanedUp = s.replace("\n", "");
            cleanedUp = cleanedUp.trim();
            cleanedUp = cleanedUp.split("<")[0];
            if (cleanedUp.contains(distanceIndicater)){
                cleanedUp = cleanedUp.split(" miles")[0];
                rideDistance = Integer.parseInt(cleanedUp.substring(distanceIndicater.length()));
            }
            if (cleanedUp.contains(tollIndicator)){
                tolls = Integer.parseInt(cleanedUp.substring(tollIndicator.length()));
            }
            if (cleanedUp.contains(minuteIndicator)){
                rideDurationMinutes = Integer.parseInt(cleanedUp.substring(minuteIndicator.length()));
            }
            else {
                if (cleanedUp.contains("Amenities")){
                    String[] splitContentAgain = cleanedUp.split("\\$");
                    amenities += Double.parseDouble(splitContentAgain[splitContentAgain.length-1]);
                }
            }

        }
        return content;
    }

    /**
     *
     * @param url parses the rating url and sets required values
     */
    void parseRatingURL(String url){
        URLConnection connect = null;
        String urlContent = requestURLPageContent(url);
        String[] contentArray = urlContent.split("<");
        int goldenStartCount = 0;
        for (String str: contentArray){
            if (str.contains("golden-star")){
                goldenStartCount++;
            }
        }
        rating = goldenStartCount;
    }

    /**
     *
     * @return fare amount in USD
     */
    double getFare() {
        return fare;
    }

    /**
     *
     * @return ride number  10 characters long and consists of numbers and capital letters “[A-Z0-9
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
     * @return location rider is going to
     */
    Location getToLocation() {
        return toLocation;
    }

    /**
     *
     * @return passengers name (aka riders name)
     */
    String getRiderName() {
        return riderName;
    }

    /**
     *
     * @return DistanceTimeTollInfoURL as a string
     */
    String getDistanceTimeTollInfoURL() {
        return distanceTimeTollInfoURL;
    }

    /**
     *
     * @return url of the rating page
     */
    String getRatingURL() {
        return ratingURL;
    }

    /**
     *
     * @return what the passenger rated the driver (out of 5 stars)
     */
    int getRating() {
        return rating;
    }

    /**
     *
     * @return the url of the original fare request page
     */
    String getFareRequestURL() {
        return fareRequestURL;
    }

    /**
     *
     * @return tolls driver had to pay during ride. always a flat dollar amount
     */
    int getTolls() {
        return tolls;
    }

    /**
     *
     * @return duration of ride in minutes
     */
    int getRideDurationMinutes() {
        return rideDurationMinutes;
    }

    /**
     *
     * @return distance of ride as an int in miles
     */
    int getRideDistance() {
        return rideDistance;
    }

    /**
     *
     * @param url is the url of the page content we a requeting
     * @return a string of all the HTML
     */
    String requestURLPageContent(String url) {
        String urlContent = null;
        URLConnection connect = null;
        InputStream inStream = null;
        try {
            connect = new URL(url).openConnection();
            inStream = connect.getInputStream();
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\Z");
            urlContent = scanner.next();
            return urlContent;
        } catch (IOException e) {
            System.out.println("Cannot establish connection to " + url + " error= " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Got Error reply on " + url + " error= " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    //do nothing here
                }
            }
        }

    }

    /**
     *
     * @return this webreader as a toString
     */
    @Override
    public String toString(){
        return "WebReader. HashCode " + this.hashCode();
    }


    /**
     *
     * @return amenities as a double for premium drivers
     */
    double getAmenities(){
        return amenities;
    }
}
