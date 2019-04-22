package uberjava;

public class UberStatistics{
    private int ratingTotal = 0;
    private double ratingAverage = 0.0;
    private int totalRides = 0;


    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the UberStatistics class? (y/n)");
    }

    /**
     * constructor
     */
    UberStatistics(){
    }

    /**
     *
     * @param rating adds to the uber drivers overall statistics
     */
    void addRating(int rating){
        totalRides++;
        ratingTotal += rating;
        ratingAverage = ((double)ratingTotal)/((double)totalRides);
    }

    /**
     *
     * @return sum of all ratings
     */
    public int getRatingTotal() {
        return ratingTotal;
    }


    /**
     *
     * @return average of all ratings
     */
    public double getRatingAverage() {
        return ratingAverage;
    }

    /**
     *
     * @return total number of rides
     */
    public int getTotalRides() {
        return totalRides;
    }

    @Override
    public String toString(){
        return "UberStatistics:\n Total Rides: " + totalRides + "\n" + "Avg Rating: " + getRatingAverage();
    }
}