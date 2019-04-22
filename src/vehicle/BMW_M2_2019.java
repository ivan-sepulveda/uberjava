package vehicle;

public class BMW_M2_2019 extends Car {
    /**
     *
     * @param args unused argument. main method meant to test code
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the BMW_M2_2019 class? (y/n)");

    }

    /**
     * constructor
     */
    public BMW_M2_2019() {
        super("M2", 2019, "BMW");
        this.setMilesPerGallon(18);
        this.setpurchaseCost(58900);
        this.setOperatingPricePerMinute();
    }
    /**
     *
     * @return the objects toString. "Car: " + className because classname contains all relevant info;
     */
    @Override
    public String toString(){
        return "Car: " + this.getClass().getSimpleName();
    }
}
